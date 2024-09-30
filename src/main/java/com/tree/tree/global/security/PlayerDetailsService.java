package com.tree.tree.global.security;

import com.tree.tree.player.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class PlayerDetailsService implements ReactiveUserDetailsService {

    private final PlayerRepository playerRepository;

    @Override
    public Mono<UserDetails> findByUsername(final String username) {
        return playerRepository.findByNickName(username)
                .filter(player -> !player.getIsDeleted())
                .map(player -> new PlayerDetails(
                        player.getNickName(),
                        player.getPassword(),
                        player.getRoles().stream()
                                .map(role -> "ROLE_" + role)
                                .toList()
                ));
    }
}
