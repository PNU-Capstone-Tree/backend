package com.tree.tree.ranking.dto.response;

import com.tree.tree.ranking.Ranking;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class RankingResponse {
    private Long rankNumber;
    private Long playerId;
    private String nickName;
    private Long maxScore;

    public static RankingResponse from(Ranking ranking, String nickName) {
        return RankingResponse.builder()
                .rankNumber(ranking.getRankNumber())
                .playerId(ranking.getPlayerId())
                .nickName(nickName)
                .maxScore(ranking.getMaxScore())
                .build();
    }
}
