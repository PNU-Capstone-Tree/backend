package com.tree.tree.ranking.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RankingUpdateRequest {

    @NotNull(message = "점수를 입력해주세요.")
    @Positive
    private Long maxScore;
}
