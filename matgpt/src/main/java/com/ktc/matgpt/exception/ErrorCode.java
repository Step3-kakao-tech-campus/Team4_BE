package com.ktc.matgpt.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    API_CONTENT_NOT_FOUND(BAD_REQUEST, 400, "[ChatGPT API] 응답을 생성하는데 실패했습니다."),
    API_REQUEST_TIMEOUT(REQUEST_TIMEOUT, 408, "[ChatGPT API] API 호출 시간이 초과됐습니다."),
    API_UNKNOWN_FINISH_REASON(INTERNAL_SERVER_ERROR, 500, "[ChatGPT API] 알 수 없는 이유로 응답을 불러올 수 없습니다.");

    private final HttpStatus httpStatus;
    private final int code;
    private final String message;
}
