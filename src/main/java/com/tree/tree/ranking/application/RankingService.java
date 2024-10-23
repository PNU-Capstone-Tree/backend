package com.tree.tree.ranking.application;

import static com.tree.tree.ranking.exception.RankingExceptionType.NOT_FOUND_RANKING;

import com.tree.tree.player.Player;
import com.tree.tree.player.repository.PlayerRepository;
import com.tree.tree.ranking.Ranking;
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

    private final RankingValidateAndFindService rankingValidateAndFindService;

    public Flux<RankingResponse> getAllRankings(final int page, final int size) {
        return rankingRepository.findAllByOrderByRankNumberAsc(PageRequest.of(page, size))
                .flatMap(this::mapToRankingResponse);
    }

    public Mono<Void> createRanking(final RankingCreateRequest rankingRequest, final Player tokenPlayer) {
        return rankingValidateAndFindService.validateAndGetPlayerNickName(rankingRequest.getNickName(), tokenPlayer)
                .flatMap(player -> processRankingCreation(player, rankingRequest))
                .then(reassignRankNumbers());
    }

    public Mono<Void> updateRanking(final Long playerId, final RankingUpdateRequest request, final Player tokenPlayer) {
        return rankingValidateAndFindService.validateAndGetPlayer(playerId, tokenPlayer)
                .flatMap(player -> rankingRepository.findByPlayerId(playerId)
                        .flatMap(ranking -> updateExistingRanking(ranking, request)))
                .then(reassignRankNumbers());
    }

    public Mono<Void> deleteRanking(final Long playerId) {
        return rankingRepository.findByPlayerId(playerId)
                .switchIfEmpty(Mono.error(() -> new RankingException(NOT_FOUND_RANKING)))
                .flatMap(rankingRepository::delete)
                .then(reassignRankNumbers());
    }

    private Mono<Ranking> processRankingCreation(final Player player, final RankingCreateRequest rankingRequest) {
        return rankingRepository.findByPlayerIdAndIsDeletedFalse(player.getId())
                .flatMap(existingRanking -> {
                    if (rankingRequest.getMaxScore() < existingRanking.getMaxScore()) {
                        return changeExistingRankingAsDeleted(existingRanking)
                                .then(saveNewRanking(player, rankingRequest));
                    } else return Mono.just(existingRanking);})
                .switchIfEmpty(saveNewRanking(player, rankingRequest));
    }

    private Mono<Ranking> changeExistingRankingAsDeleted(final Ranking existingRanking) {
        Ranking updatedExistingRanking = existingRanking.toBuilder()
                .isDeleted(true)
                .build();
        return rankingRepository.save(updatedExistingRanking);
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
        if (request.getMaxScore() < ranking.getMaxScore()) {
            Ranking updatedRanking = ranking.toBuilder()
                    .maxScore(request.getMaxScore())
                    .build();
            return rankingRepository.save(updatedRanking);
        } else {
            return Mono.just(ranking);
        }
    }

    private Mono<RankingResponse> mapToRankingResponse(final Ranking ranking) {
        return playerRepository.findById(ranking.getPlayerId())
                .map(player -> RankingResponse.from(ranking, player.getNickName()));
    }
}
