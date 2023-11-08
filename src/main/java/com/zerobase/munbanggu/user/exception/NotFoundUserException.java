package com.zerobase.munbanggu.user.exception;

import lombok.Getter;

@Getter
public class NotFoundUserException extends RuntimeException {

    private final ErrorCode errorCode;

    public NotFoundUserException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
