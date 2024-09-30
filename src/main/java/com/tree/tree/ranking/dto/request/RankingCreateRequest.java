package com.tree.tree.ranking.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RankingCreateRequest {

    private static final int MAX_NICKNAME_LENGTH = 45;

    @NotNull(message = "닉네임을 입력해주세요.")
    @Size(max = MAX_NICKNAME_LENGTH)
    private String nickName;

    @NotNull(message = "점수를 입력해주세요.")
    @Positive
    private Long maxScore;
}
