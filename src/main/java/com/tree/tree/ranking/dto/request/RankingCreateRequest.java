package com.tree.tree.ranking.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RankingCreateRequest {
    @NotNull(message = "닉네임을 입력해주세요.")
    private String nickName;

    @NotNull(message = "점수를 입력해주세요.")
    @Positive
    private Long maxScore;
}
