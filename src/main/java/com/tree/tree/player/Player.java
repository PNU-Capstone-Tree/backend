package com.tree.tree.player;


import com.tree.tree.config.BaseEntity;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table("player")
public class Player extends BaseEntity {

    @Id
    @Generated
    private Long id;

    @NotNull
    @Column("password")
    private String password;

    @NotNull
    @Column("nickname")
    private String nickName;

    @NotNull
    @Column("is_deleted")
    private Boolean isDeleted;

    @Column("roles")
    private List<String> roles;

    @Builder
    private Player(@NotNull final String password, @NotNull final String nickName, @NotNull final Boolean isDeleted,
                   @NotNull final List<String> roles) {
        this.password = password;
        this.nickName = nickName;
        this.isDeleted = isDeleted;
        this.roles = roles;
    }
}
