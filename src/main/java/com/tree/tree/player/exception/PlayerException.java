package com.tree.tree.player.exception;

import com.tree.tree.global.base.BaseException;
import com.tree.tree.global.base.BaseExceptionType;

public class PlayerException extends BaseException {

    private final PlayerExceptionType playerExceptionType;

    public PlayerException(final PlayerExceptionType playerExceptionType) {
        super(playerExceptionType.errorMessage());
        this.playerExceptionType = playerExceptionType;
    }

    @Override
    public BaseExceptionType exceptionType() {
        return playerExceptionType;
    }
}
