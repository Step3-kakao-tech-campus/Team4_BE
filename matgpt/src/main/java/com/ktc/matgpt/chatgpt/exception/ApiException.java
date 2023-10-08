package com.ktc.matgpt.chatgpt.exception;

import com.ktc.matgpt.exception.CustomException;
import com.ktc.matgpt.exception.ErrorCode;

public class ApiException extends CustomException {

    public ApiException(ErrorCode errorCode) {
        super(errorCode);
    }

    public static class TimeoutException extends ApiException {
        public TimeoutException() {
            super(ErrorCode.API_REQUEST_TIMEOUT);
        }
    }

    public static class ContentNotFoundException extends ApiException {
        public ContentNotFoundException() {
            super(ErrorCode.API_CONTENT_NOT_FOUND);
        }
    }

    public static class UnknownFinishReasonException extends ApiException {
        public UnknownFinishReasonException() {
            super(ErrorCode.API_UNKNOWN_FINISH_REASON);
        }
    }
}
