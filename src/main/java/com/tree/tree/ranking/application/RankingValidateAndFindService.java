package com.tree.tree.ranking.application;

import static com.tree.tree.player.exception.PlayerExceptionType.NOT_FOUND_PLAYER;
import static com.tree.tree.ranking.exception.RankingExceptionType.CANNOT_UPDATE_RANKING;

import com.tree.tree.player.Player;
import com.tree.tree.player.exception.PlayerException;
import com.tree.tree.player.repository.PlayerRepository;
import com.tree.tree.ranking.exception.RankingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class RankingValidateAndFindService {
    private final PlayerRepository playerRepository;

    public Mono<Player> findPlayerByNickName(final String nickName) {
        return playerRepository.findByNickName(nickName)
                .switchIfEmpty(Mono.error(() -> new PlayerException(NOT_FOUND_PLAYER)));
    }

    private Mono<Player> findPlayerById(final Long playerId) {
        return playerRepository.findById(playerId)
                .switchIfEmpty(Mono.error(() -> new PlayerException(NOT_FOUND_PLAYER)));
    }

    public Mono<Player> validateAndGetPlayerNickName(final String nickName, final Player tokenPlayer) {
        return findPlayerByNickName(nickName)
                .flatMap(player -> {
                    if (!player.getId().equals(tokenPlayer.getId())) {
                        return Mono.error(() -> new RankingException(CANNOT_UPDATE_RANKING));
                    }
                    return Mono.just(player);
                });
    }

    public Mono<Player> validateAndGetPlayer(final Long playerId, final Player tokenPlayer) {
        if (!tokenPlayer.getId().equals(playerId)) {
            return Mono.error(() -> new RankingException(CANNOT_UPDATE_RANKING));
        }

        return findPlayerById(playerId)
                .flatMap(player -> {
                    if (!tokenPlayer.getNickName().equals(player.getNickName())) {
                        return Mono.error(() -> new RankingException(CANNOT_UPDATE_RANKING));
                    }
                    return Mono.just(player);
                });
    }
}
