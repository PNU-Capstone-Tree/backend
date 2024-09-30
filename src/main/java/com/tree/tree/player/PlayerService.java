package com.tree.tree.player;

import static com.tree.tree.player.exception.PlayerExceptionType.ALREADY_EXIST_NICKNAME;
import static com.tree.tree.player.exception.PlayerExceptionType.CANNOT_MATCH_PASSWORD;
import static com.tree.tree.player.exception.PlayerExceptionType.NOT_FOUND_NICKNAME;

import com.tree.tree.global.security.JwtProvider;
import com.tree.tree.player.dto.request.SignInRequest;
import com.tree.tree.player.dto.request.SignUpRequest;
import com.tree.tree.player.dto.response.TokenResponse;
import com.tree.tree.player.exception.PlayerException;
import com.tree.tree.player.repository.PlayerRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class PlayerService {

    private final PlayerRepository playerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public Mono<Void> signUp(final SignUpRequest signupRequest) {
        return playerRepository.findByNickName(signupRequest.getNickName())
                .flatMap(player -> Mono.error(() -> new PlayerException(ALREADY_EXIST_NICKNAME)))
                .switchIfEmpty(playerRepository.save(Player.builder()
                        .password(passwordEncoder.encode(signupRequest.getPassword()))
                        .nickName(signupRequest.getNickName())
                        .isDeleted(false)
                        .roles(List.of("PLAYER"))
                        .build()))
                .then();
    }

    public Mono<TokenResponse> signIn(final SignInRequest signInRequest) {
        return playerRepository.findByNickName(signInRequest.getNickName())
                .switchIfEmpty(Mono.error(() -> new PlayerException(NOT_FOUND_NICKNAME)))
                .flatMap(player -> {
                    if (!passwordEncoder.matches(signInRequest.getPassword(), player.getPassword())) {
                        return Mono.error(() -> new PlayerException(CANNOT_MATCH_PASSWORD));
                    }
                    final String token = jwtProvider.createToken(player.getNickName(), List.of("PLAYER"));
                    return Mono.just(new TokenResponse(token));
                });
    }
}
