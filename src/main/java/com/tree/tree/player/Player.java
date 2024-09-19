package com.tree.tree.player;


import com.tree.tree.config.BaseEntity;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table("player")
public class Player extends BaseEntity {

    @Id
    private Long id;

    @NotNull
    private String password;

    @NotNull
    private String nickName;

    @NotNull
    private Boolean isDeleted;

    @Builder
    private Player(@NotNull final String password, @NotNull final String nickName, @NotNull final Boolean isDeleted) {
        this.password = password;
        this.nickName = nickName;
        this.isDeleted = isDeleted;
    }
}
