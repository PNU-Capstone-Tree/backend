package com.tree.tree.player;

import com.tree.tree.player.dto.request.SignInRequest;
import com.tree.tree.player.dto.request.SignUpRequest;
import com.tree.tree.player.dto.response.TokenResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Validated
@RestController
@RequiredArgsConstructor
public class PlayerController {

    private final PlayerService playerService;

    @PostMapping("/signUp")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Void> signup(@RequestBody @Valid final SignUpRequest signupRequest) {
        return playerService.signUp(signupRequest);
    }

    @PostMapping("/signIn")
    public Mono<TokenResponse> login(@RequestBody @Valid final SignInRequest signInRequest) {
        return playerService.signIn(signInRequest);
    }
}
