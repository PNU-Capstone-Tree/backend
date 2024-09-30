package com.tree.tree.player.dto.request;

import static com.tree.tree.player.Player.NICKNAME_INVALID;
import static com.tree.tree.player.Player.NICKNAME_REGEX;
import static com.tree.tree.player.Player.PASSWORD_INVALID;
import static com.tree.tree.player.Player.PASSWORD_REGEX;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SignInRequest {

    private static final int MAX_NICKNAME_LENGTH = 45;
    private static final int MAX_PASSWORD_LENGTH = 100;

    @NotNull(message = "닉네임을 입력해주세요.")
    @Size(max = MAX_NICKNAME_LENGTH)
    @Pattern(regexp = NICKNAME_REGEX, message = NICKNAME_INVALID)
    private String nickName;

    @NotNull(message = "비밀번호를 입력해주세요.")
    @Size(max = MAX_PASSWORD_LENGTH)
    @Pattern(regexp = PASSWORD_REGEX, message = PASSWORD_INVALID)
    private String password;
}
