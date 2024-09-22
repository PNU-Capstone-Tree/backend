package com.tree.tree.ranking.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RankingUpdateRequest {
    @NotNull(message = "점수를 입력해주세요.")
    @Positive
    private Long maxScore;
}
