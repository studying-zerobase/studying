package com.zerobase.munbanggu.user.exception;

import io.jsonwebtoken.JwtException;
import lombok.Getter;

@Getter
public class InvalidTokenException extends JwtException {

    private final ErrorCode errorCode;

    public InvalidTokenException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
