package com.tree.tree.ranking.exception;

import com.tree.tree.global.base.BaseExceptionType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum RankingExceptionType implements BaseExceptionType {
    NOT_FOUND_RANKING(HttpStatus.NOT_FOUND, "랭킹을 찾을 수 없습니다."),
    CANNOT_UPDATE_RANKING(HttpStatus.BAD_REQUEST, "랭킹은 본인만 수정할 수 있습니다."),
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
