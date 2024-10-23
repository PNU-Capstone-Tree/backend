package com.tree.tree.ranking;

import com.tree.tree.global.security.annotation.LoginPlayer;
import com.tree.tree.player.Player;
import com.tree.tree.ranking.dto.request.RankingCreateRequest;
import com.tree.tree.ranking.dto.request.RankingUpdateRequest;
import com.tree.tree.ranking.dto.response.RankingResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/rankings")
public class RankingController {

    private final RankingService rankingService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Flux<RankingResponse> getAllRankings(
            @RequestParam(defaultValue = "0") @PositiveOrZero final int page,
            @RequestParam(defaultValue = "10") @PositiveOrZero final int size) {
        return rankingService.getAllRankings(page, size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('PLAYER')")
    public Mono<Void> createRanking(@RequestBody @Valid final RankingCreateRequest rankingRequest,
                                    @LoginPlayer Player tokenPlayer) {
        return rankingService.createRanking(rankingRequest, tokenPlayer);
    }

    @PutMapping("/{playerId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('PLAYER')")
    public Mono<Void> updateRanking(@PathVariable final Long playerId,
                                    @RequestBody @Valid final RankingUpdateRequest request,
                                    @LoginPlayer final Player tokenPlayer) {
        return rankingService.updateRanking(playerId, request, tokenPlayer);
    }

    @DeleteMapping("/{playerId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<Void> deleteRanking(@PathVariable final Long playerId) {
        return rankingService.deleteRanking(playerId);
    }
}
