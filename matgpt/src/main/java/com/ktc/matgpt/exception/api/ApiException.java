package com.ktc.matgpt.exception.api;

import com.ktc.matgpt.exception.ErrorMessage;

public class ApiException extends RuntimeException {

    public ApiException(String message) {
        super(message);
    }

    public static class TimeoutException extends ApiException {
        public TimeoutException() {
            super(ErrorMessage.API_REQUEST_TIMEOUT);
        }
    }

    public static class ContentNotFoundException extends ApiException {
        public ContentNotFoundException() {
            super(ErrorMessage.API_CONTENT_NOT_FOUND);
        }
    }

    public static class UnknownFinishReasonException extends ApiException {
        public UnknownFinishReasonException() {
            super(ErrorMessage.API_UNKNOWN_FINISH_REASON);
        }
    }
}
