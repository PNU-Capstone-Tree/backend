package com.tree.tree.ranking.repository;

import com.tree.tree.ranking.Ranking;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RankingRepository extends ReactiveCrudRepository<Ranking, Long> {
    Flux<Ranking> findAllByOrderByRankNumberAsc(Pageable pageable);
    Flux<Ranking> findAllByOrderByMaxScoreDesc();
    Mono<Ranking> findByPlayerId(final Long playerId);
}
