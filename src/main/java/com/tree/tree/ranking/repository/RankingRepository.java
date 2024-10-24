package com.tree.tree.ranking.repository;

import com.tree.tree.ranking.Ranking;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RankingRepository extends ReactiveCrudRepository<Ranking, Long> {
    @Query("SELECT * FROM ranking WHERE is_deleted = false")
    Flux<Ranking> findAllByOrderByRankNumberAsc(Pageable pageable);
    Flux<Ranking> findAllByOrderByMaxScoreAsc();
    Mono<Ranking> findByPlayerId(final Long playerId);
    Mono<Ranking> findByPlayerIdAndIsDeletedFalse(Long playerId);
}
