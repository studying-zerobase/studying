package com.zerobase.munbanggu.studyboard.exception;

import com.zerobase.munbanggu.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class StudyBoardExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> notFoundPostExceptionHandler(NotFoundPostException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(e.getErrorCode(), e.getMessage()));
    }
}
