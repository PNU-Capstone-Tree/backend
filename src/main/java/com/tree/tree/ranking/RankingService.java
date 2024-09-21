package com.tree.tree.ranking;

import com.tree.tree.player.repository.PlayerRepository;
import com.tree.tree.ranking.dto.response.RankingResponse;
import com.tree.tree.ranking.repository.RankingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

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
}
