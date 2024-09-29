package com.tree.tree.player;

import com.tree.tree.config.security.JwtProvider;
import com.tree.tree.player.dto.request.SignInRequest;
import com.tree.tree.player.dto.request.SignUpRequest;
import com.tree.tree.player.dto.response.TokenResponse;
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
                .flatMap(player -> Mono.error(new RuntimeException("이미 존재하는 닉네임입니다: " + signupRequest.getNickName())))
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
                .switchIfEmpty(Mono.error(new RuntimeException("존재하지 않는 닉네임입니다.")))
                .flatMap(player -> {
                    if (!passwordEncoder.matches(signInRequest.getPassword(), player.getPassword())) {
                        return Mono.error(new RuntimeException("비밀번호가 일치하지 않습니다."));
                    }
                    final String token = jwtProvider.createToken(player.getNickName(), List.of("PLAYER"));
                    return Mono.just(new TokenResponse(token));
                });
    }
}
