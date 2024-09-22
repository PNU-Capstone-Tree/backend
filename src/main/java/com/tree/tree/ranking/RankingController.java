package com.tree.tree.ranking;

import com.tree.tree.ranking.dto.request.RankingCreateRequest;
import com.tree.tree.ranking.dto.response.RankingResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
    public Flux<RankingResponse> getAllRankings(
            @RequestParam(defaultValue = "0") @PositiveOrZero final int page,
            @RequestParam(defaultValue = "10") @PositiveOrZero final int size) {
        return rankingService.getAllRankings(page, size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Void> createRanking(@RequestBody @Valid RankingCreateRequest rankingRequest) {
        return rankingService.createRanking(rankingRequest);
    }
}
