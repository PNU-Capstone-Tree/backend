package com.tree.tree.ranking;

import com.tree.tree.player.repository.PlayerRepository;
import com.tree.tree.ranking.dto.request.RankingRequest;
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

    public Mono<Void> createRanking(RankingRequest rankingRequest) {
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

    private Mono<Void> reassignRankNumbers() {
        return rankingRepository.findAllByOrderByMaxScoreDesc()
                .filter(ranking -> !ranking.getIsDeleted())
                .index()
                .flatMap(tuple -> rankingRepository.save(
                        tuple.getT2().toBuilder()
                                .rankNumber(tuple.getT1() + 1)
                                .build()
                ))
                .then();
    }
}
