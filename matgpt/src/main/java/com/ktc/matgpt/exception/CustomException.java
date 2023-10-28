package com.ktc.matgpt.exception;

import com.ktc.matgpt.utils.ApiUtils;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomException extends RuntimeException {

    private final ErrorCode errorCode;

    public CustomException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public ApiUtils.ApiFail body() {
        return ApiUtils.fail(errorCode.getCode(), errorCode.getMessage());
    }

    public HttpStatus status() {
        return errorCode.getHttpStatus();
    }
}
