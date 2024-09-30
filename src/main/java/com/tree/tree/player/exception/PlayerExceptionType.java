package com.tree.tree.player.exception;

import com.tree.tree.global.base.BaseExceptionType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum PlayerExceptionType implements BaseExceptionType {
    NOT_FOUND_PLAYER(HttpStatus.NOT_FOUND, "플레이어를 찾을 수 없습니다.");

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
