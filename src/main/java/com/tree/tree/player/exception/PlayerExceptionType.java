package com.tree.tree.player.exception;

import com.tree.tree.global.base.BaseExceptionType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum PlayerExceptionType implements BaseExceptionType {
    NOT_FOUND_PLAYER(HttpStatus.NOT_FOUND, "플레이어를 찾을 수 없습니다."),
    NOT_FOUND_NICKNAME(HttpStatus.NOT_FOUND, "닉네임을 찾을 수 없습니다."),
    ALREADY_EXIST_NICKNAME(HttpStatus.BAD_REQUEST, "이미 존재하는 닉네임입니다."),
    CANNOT_MATCH_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다.")
    ;

    private final HttpStatus httpStatus;
    private final String errorMessage;

    @Override
    public HttpStatus httpStatus() {
        return httpStatus;
    }

    @Override
    public String errorMessage() {
        return errorMessage;
    }
}
