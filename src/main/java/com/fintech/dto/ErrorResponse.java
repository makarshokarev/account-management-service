package com.fintech.dto;

import com.fintech.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ErrorResponse {
    private ErrorCode errorCode;
    private String message;
    private LocalDateTime timestamp;

    public static ErrorResponse of(ErrorCode errorCode, String message) {
        return new ErrorResponse(errorCode, message, LocalDateTime.now());
    }
}
