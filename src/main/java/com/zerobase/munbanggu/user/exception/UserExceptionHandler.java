package com.zerobase.munbanggu.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UserExceptionHandler {

    @ExceptionHandler(EmailConflictException.class)
    public ResponseEntity<ErrorCode> emailConflictExceptionHandler(EmailConflictException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getErrorCode());
    }
}
