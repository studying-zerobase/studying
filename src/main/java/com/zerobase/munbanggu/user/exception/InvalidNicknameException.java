package com.zerobase.munbanggu.user.exception;

public class InvalidNicknameException extends RuntimeException {
    public InvalidNicknameException(String message) {
        super(message);
    }
}