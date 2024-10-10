package com.tree.tree.player;


import com.tree.tree.global.base.BaseEntity;
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

    public static final String NICKNAME_INVALID = "닉네임은 2~20자 한글, 영어만 가능합니다.";
    public static final String NICKNAME_REGEX = "^[a-zA-Z가-힣]{2,20}";

    public static final String PASSWORD_INVALID = "비밀번호는 8~16자여야 하고 영어, 숫자, 특수문자가 포함되어야 합니다.";
    public static final String PASSWORD_REGEX = "^(?=.*?[A-Za-z])(?=.*?\\d)(?=.*?[!@#$%^&*(),.-]).{8,16}$";

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
