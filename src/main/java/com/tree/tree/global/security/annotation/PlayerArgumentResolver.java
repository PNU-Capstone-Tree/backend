package com.tree.tree.global.security.annotation;

import static com.tree.tree.player.exception.PlayerExceptionType.NOT_FOUND_PLAYER;

import com.tree.tree.global.security.PlayerDetails;
import com.tree.tree.player.exception.PlayerException;
import com.tree.tree.player.repository.PlayerRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.BindingContext;
import org.springframework.web.reactive.result.method.HandlerMethodArgumentResolver;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class PlayerArgumentResolver implements HandlerMethodArgumentResolver {

    private final PlayerRepository playerRepository;

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginPlayer.class);
    }

    @NotNull
    @Override
    public Mono<Object> resolveArgument(@NotNull final MethodParameter parameter,
                                        @NotNull final BindingContext bindingContext,
                                        @NotNull final ServerWebExchange exchange) {

        return ReactiveSecurityContextHolder.getContext()
                .flatMap(securityContext -> {
                    Object principal = securityContext.getAuthentication().getPrincipal();
                    if (principal instanceof PlayerDetails) {
                        Long loginPlayerId = ((PlayerDetails) principal).getPlayerId();
                        return playerRepository.findById(loginPlayerId)
                                .switchIfEmpty(Mono.error(() -> new PlayerException(NOT_FOUND_PLAYER)));
                    } else {
                        return Mono.error(() -> new PlayerException(NOT_FOUND_PLAYER));
                    }
                });
    }

}
