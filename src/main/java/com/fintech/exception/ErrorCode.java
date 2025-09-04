package com.fintech.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    ACCOUNT_NOT_FOUND("ACCOUNT_NOT_FOUND"),
    DUPLICATE_PHONE_NUMBER("DUPLICATE_PHONE_NUMBER"),
    VALIDATION_FAILED("VALIDATION_FAILED"),
    INVALID_REQUEST("INVALID_REQUEST"),
    ACCESS_DENIED("ACCESS_DENIED"),
    INTERNAL_ERROR("INTERNAL_ERROR");


    private final String code;
}
