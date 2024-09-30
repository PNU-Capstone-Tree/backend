package com.tree.tree.ranking;

import com.tree.tree.global.base.BaseEntity;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table("ranking")
@SuperBuilder(toBuilder = true)
public class Ranking extends BaseEntity {

    @Id
    @Generated
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
}
