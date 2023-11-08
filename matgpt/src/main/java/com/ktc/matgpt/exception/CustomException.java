package com.ktc.matgpt.exception;

import com.ktc.matgpt.utils.ApiUtils;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomException extends RuntimeException {

    private final ErrorCode errorCode;
    private String causedBy = "";

    public CustomException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public CustomException(ErrorCode errorCode, String causedBy) {
        this.errorCode = errorCode;
        this.causedBy = causedBy;
    }

    public ApiUtils.ApiFail body() {
        if (this.causedBy.isEmpty()) {
           return ApiUtils.fail(errorCode.getCode(), errorCode.getMessage());
        }
        return ApiUtils.fail(errorCode.getCode(), errorCode.getMessage() + ": " + causedBy);
    }

    public HttpStatus status() {
        return errorCode.getHttpStatus();
    }
}
