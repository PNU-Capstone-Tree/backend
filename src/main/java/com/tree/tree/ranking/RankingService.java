package com.tree.tree.ranking;

import static com.tree.tree.player.exception.PlayerExceptionType.NOT_FOUND_PLAYER;
import static com.tree.tree.ranking.exception.RankingExceptionType.CANNOT_UPDATE_RANKING;
import static com.tree.tree.ranking.exception.RankingExceptionType.NOT_FOUND_RANKING;

import com.tree.tree.player.Player;
import com.tree.tree.player.exception.PlayerException;
import com.tree.tree.player.repository.PlayerRepository;
import com.tree.tree.ranking.dto.request.RankingCreateRequest;
import com.tree.tree.ranking.dto.request.RankingUpdateRequest;
import com.tree.tree.ranking.dto.response.RankingResponse;
import com.tree.tree.ranking.exception.RankingException;
import com.tree.tree.ranking.repository.RankingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class RankingService {
    private final RankingRepository rankingRepository;
    private final PlayerRepository playerRepository;

    public Flux<RankingResponse> getAllRankings(final int page, final int size) {
        return rankingRepository.findAllByOrderByRankNumberAsc(PageRequest.of(page, size))
                .flatMap(this::mapToRankingResponse);
    }

    public Mono<Void> createRanking(final RankingCreateRequest rankingRequest) {
        return findPlayerByNickName(rankingRequest.getNickName())
                .flatMap(player -> saveNewRanking(player, rankingRequest))
                .then(reassignRankNumbers()); // rankNumber 재부여
    }

    public Mono<Void> updateRanking(final Long playerId, final RankingUpdateRequest request, final Player tokenPlayer) {
        return validateAndGetPlayer(playerId, tokenPlayer)
                .flatMap(player -> rankingRepository.findByPlayerId(playerId)
                        .flatMap(ranking -> updateExistingRanking(ranking, request))
                        .switchIfEmpty(createNewRanking(player, request))
                )
                .then(reassignRankNumbers());
    }

    public Mono<Void> deleteRanking(final Long playerId) {
        return rankingRepository.findByPlayerId(playerId)
                .switchIfEmpty(Mono.error(() -> new RankingException(NOT_FOUND_RANKING)))
                .flatMap(rankingRepository::delete)
                .then(reassignRankNumbers());
    }

    private Mono<Void> reassignRankNumbers() {
        return rankingRepository.findAllByOrderByMaxScoreAsc()
                .filter(ranking -> !ranking.getIsDeleted())
                .index()
                .flatMap(tuple -> {
                    Long newRankNumber = tuple.getT1() + 1;
                    return rankingRepository.save(tuple.getT2().toBuilder()
                            .rankNumber(newRankNumber)
                            .build());
                })
                .then();
    }

    private Mono<Player> findPlayerByNickName(final String nickName) {
        return playerRepository.findByNickName(nickName)
                .switchIfEmpty(Mono.error(() -> new PlayerException(NOT_FOUND_PLAYER)));
    }

    private Mono<Player> findPlayerById(final Long playerId) {
        return playerRepository.findById(playerId)
                .switchIfEmpty(Mono.error(() -> new PlayerException(NOT_FOUND_PLAYER)));
    }

    private Mono<Ranking> saveNewRanking(final Player player, final RankingCreateRequest rankingRequest) {
        final Ranking ranking = Ranking.builder()
                .playerId(player.getId())
                .maxScore(rankingRequest.getMaxScore())
                .rankNumber(0L) // 임시값
                .isDeleted(false)
                .build();
        return rankingRepository.save(ranking);
    }

    private Mono<Ranking> updateExistingRanking(final Ranking ranking, final RankingUpdateRequest request) {
        if (request.getMaxScore() > ranking.getMaxScore()) {
            Ranking updatedRanking = ranking.toBuilder()
                    .maxScore(request.getMaxScore())
                    .build();
            return rankingRepository.save(updatedRanking);
        } else {
            return Mono.just(ranking);
        }
    }

    private Mono<Ranking> createNewRanking(final Player player, final RankingUpdateRequest request) {
        RankingCreateRequest createRequest = RankingCreateRequest.builder()
                .nickName(player.getNickName())
                .maxScore(request.getMaxScore())
                .build();
        return saveNewRanking(player, createRequest);
    }

    private Mono<RankingResponse> mapToRankingResponse(final Ranking ranking) {
        return playerRepository.findById(ranking.getPlayerId())
                .map(player -> RankingResponse.from(ranking, player.getNickName()));
    }

    private Mono<Player> validateAndGetPlayer(final Long playerId, final Player tokenPlayer) {
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
