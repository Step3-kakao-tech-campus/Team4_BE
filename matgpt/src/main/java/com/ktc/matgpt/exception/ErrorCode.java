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
    API_UNKNOWN_FINISH_REASON(INTERNAL_SERVER_ERROR, 500, "[ChatGPT API] 알 수 없는 이유로 응답을 불러올 수 없습니다."),
    STORE_NOT_FOUND(NOT_FOUND, 404, "[MatGPT] 요청한 상점을 찾을 수 없습니다."),
    REVIEW_NOT_FOUND(NOT_FOUND, 404, "[MatGPT] 요청한 리뷰를 찾을 수 없습니다."),
    REVIEW_PROCESS_ERROR(BAD_REQUEST, 400, "[MatGPT] 리뷰 등록 중 오류가 발생했습니다."),
    S3_FILE_VALIDATION_ERROR(BAD_REQUEST, 400, "[MatGPT] S3파일 검증 중에 오류가 발생했습니다."),

    //Auth
    INVALID_TOKEN_EXCEPTION(INTERNAL_SERVER_ERROR, 500, "[MatGPT] 유효하지 않은 토큰입니다."),
    OAUTH2_PROCESSING_EXCEPTION(UNAUTHORIZED, 401, "[MatGPT] OAuth2 인증 처리 중 오류가 발생했습니다.");

    private final HttpStatus httpStatus;
    private final int code;
    private final String message;
}
