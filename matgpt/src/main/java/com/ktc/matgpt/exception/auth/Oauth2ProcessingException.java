package com.ktc.matgpt.exception.auth;

import com.ktc.matgpt.exception.ErrorMessage;

public class Oauth2ProcessingException extends RuntimeException {
    public Oauth2ProcessingException() {
        super(ErrorMessage.OAUTH2_PROCESSING_EXCEPTION);
    }
}
