package com.zerobase.munbanggu.dto;

import com.zerobase.munbanggu.user.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ErrorResponse {

    private ErrorCode errorCode;
    private String message;

}
