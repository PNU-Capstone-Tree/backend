package com.tree.tree.config.security;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter implements WebFilter {

    private final JwtProvider jwtProvider;

    @NotNull
    @Override
    public Mono<Void> filter(@NonNull final ServerWebExchange exchange, @NonNull final WebFilterChain chain) {
        String token = jwtProvider.resolveToken(exchange);

        if (token != null && jwtProvider.validateToken(token)) {
            return jwtProvider.getAuthentication(token)
                    .flatMap(authentication -> {
                        SecurityContextImpl securityContext = new SecurityContextImpl(authentication);
                        return chain.filter(exchange)
                                .contextWrite(
                                        ReactiveSecurityContextHolder.withSecurityContext(Mono.just(securityContext)));
                    });
        }
        return chain.filter(exchange);
    }
}

