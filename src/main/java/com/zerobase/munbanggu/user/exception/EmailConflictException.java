package com.zerobase.munbanggu.user.exception;

import lombok.Getter;

@Getter
public class EmailConflictException extends RuntimeException {

    private final ErrorCode errorCode;

    public EmailConflictException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

}
