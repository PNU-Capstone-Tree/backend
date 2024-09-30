package com.tree.tree.ranking.exception;

import com.tree.tree.config.BaseException;
import com.tree.tree.config.BaseExceptionType;

public class RankingException extends BaseException {

    private final RankingExceptionType rankingExceptionType;

    public RankingException(final RankingExceptionType rankingExceptionType) {
        super(rankingExceptionType.errorMessage());
        this.rankingExceptionType = rankingExceptionType;
    }

    @Override
    public BaseExceptionType exceptionType() {
        return rankingExceptionType;
    }
}
