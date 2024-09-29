package com.tree.tree.ranking;

import com.tree.tree.player.repository.PlayerRepository;
import com.tree.tree.ranking.dto.request.RankingCreateRequest;
import com.tree.tree.ranking.dto.request.RankingUpdateRequest;
import com.tree.tree.ranking.dto.response.RankingResponse;
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
                .flatMap(ranking -> playerRepository.findById(ranking.getPlayerId())
                        .map(player -> RankingResponse.from(ranking, player.getNickName()))
                );
    }

    public Mono<Void> createRanking(final RankingCreateRequest rankingRequest) {
        return playerRepository.findByNickName(rankingRequest.getNickName())
                //todo: error 처리
                .switchIfEmpty(Mono.error(new RuntimeException("플레이어를 찾을 수 없습니다: " + rankingRequest.getNickName())))
                .flatMap(player -> rankingRepository.save(Ranking.builder()
                        .playerId(player.getId())
                        .maxScore(rankingRequest.getMaxScore())
                        .rankNumber(0L) // 임시값 넣음
                        .build()))
                .then(reassignRankNumbers()); // rankNumber 재부여
    }

    public Mono<Void> updateRanking(final Long playerId, final RankingUpdateRequest request) {
        return playerRepository.findById(playerId)
                .switchIfEmpty(Mono.error(new RuntimeException("플레이어를 찾을 수 없습니다: " + playerId)))
                .flatMap(player -> rankingRepository.findByPlayerId(playerId)
                        .flatMap(ranking -> rankingRepository.save(ranking.toBuilder()
                                    .maxScore(request.getMaxScore())
                                    .build()))
                        .switchIfEmpty(
                                createRanking(RankingCreateRequest.builder()
                                        .nickName(player.getNickName())
                                        .maxScore(request.getMaxScore())
                                        .build())
                                        .then(Mono.empty())
                        )
                )
                .then(reassignRankNumbers()); // rankNumber 재부여
    }

    public Mono<Void> deleteRanking(final Long playerId) {
        return rankingRepository.findByPlayerId(playerId)
                .switchIfEmpty(Mono.error(new RuntimeException("랭킹을 찾을 수 없습니다: " + playerId)))
                .flatMap(rankingRepository::delete)
                .then(reassignRankNumbers());
    }

    private Mono<Void> reassignRankNumbers() {
        return rankingRepository.findAllByOrderByMaxScoreDesc()
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
}
