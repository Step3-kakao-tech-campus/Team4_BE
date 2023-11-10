package com.ktc.matgpt.exception.auth;

import com.ktc.matgpt.exception.ErrorMessage;
import org.springframework.security.core.AuthenticationException;

public class InvalidTokenException extends AuthenticationException {
    public InvalidTokenException() {
        super(ErrorMessage.INVALID_TOKEN_EXCEPTION);
    }
}
