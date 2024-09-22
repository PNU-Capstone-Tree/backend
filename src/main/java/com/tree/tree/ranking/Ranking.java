package com.tree.tree.ranking;

import com.tree.tree.config.BaseEntity;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table("ranking")
public class Ranking extends BaseEntity {

    @Id
    private Long id;

    @NotNull
    @Column("max_score")
    private Long maxScore;

    @NotNull
    @Column("rank_number")
    private Long rankNumber;

    @NotNull
    @Column("player_id")
    private Long playerId;

    @NotNull
    @Column("is_deleted")
    private Boolean isDeleted;

    @Builder(toBuilder = true)
    private Ranking(@NotNull final Long maxScore, @NotNull final Long rankNumber, @NotNull final Long playerId) {
        this.maxScore = maxScore;
        this.rankNumber = rankNumber;
        this.playerId = playerId;
        this.isDeleted = false;
    }
}
