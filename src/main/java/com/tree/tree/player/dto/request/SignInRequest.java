package com.tree.tree.player.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SignInRequest {

    @NotNull(message = "닉네임을 입력해주세요.")
    private String nickName;

    @NotNull(message = "비밀번호를 입력해주세요.")
    private String password;
}
