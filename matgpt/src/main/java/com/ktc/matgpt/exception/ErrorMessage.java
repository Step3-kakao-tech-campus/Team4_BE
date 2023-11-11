package com.ktc.matgpt.exception;

public class ErrorMessage {

    // Common
    public static final String INVALID_SORT_TYPE = "[MatGPT] 올바르지 않은 정렬 기준입니다.";
    public static final String INTERNAL_SERVER_ERROR = "[MatGPT] 내부 서버 에러가 발생했습니다.";

    // Api
    public static final String API_CONTENT_NOT_FOUND = "[ChatGPT API] 응답을 생성하는데 실패했습니다.";
    public static final String API_REQUEST_TIMEOUT ="[ChatGPT API] API 호출 시간이 초과됐습니다.";
    public static final String API_UNKNOWN_FINISH_REASON =  "[ChatGPT API] 알 수 없는 이유로 응답을 불러올 수 없습니다.";

    // Store
    public static final String STORE_NOT_FOUND = "[MatGPT] 요청한 상점을 찾을 수 없습니다.";

    // Review
    public static final String REVIEW_NOT_FOUND = "[MatGPT] 요청한 리뷰를 찾을 수 없습니다.";
    public static final String REVIEW_LIST_NOT_FOUND = "[MatGPT] 요청한 리뷰 목록이 비어 있습니다.";
    public static final String REVIEW_PROCESS_ERROR = "[MatGPT] 리뷰 등록 중 오류가 발생했습니다.";
    public static final String REVIEW_INVALID_SUMMARY_TYPE = "[MatGPT] 올바르지 않은 요약 방식입니다.";
    public static final String REVIEW_UNAUTHORIZED_ACCESS = "[MatGPT] 본인이 작성한 리뷰가 아닙니다.";
    public static final String REVIEW_INVALID_PEOPLE_COUNT = "[MatGPT] 방문인원수는 0명일 수 없습니다.";

    // S3
    public static final String S3_FILE_VALIDATION_ERROR = "[MatGPT] S3파일 검증 중에 오류가 발생했습니다.";
    public static final String INVALID_S3_URL = "[MatGPT] 유효하지 않은 S3 Url입니다.";

    // Auth
    public static final String INVALID_TOKEN_EXCEPTION = "[MatGPT] 유효하지 않은 토큰입니다.";
    public static final String UNAUTHORIZED_EXCEPTION = "[MatGPT] 유효한 인증이 필요합니다.";
    public static final String LOGOUT_USER_NOT_FOUND = "[MatGPT] 로그아웃 된 사용자입니다.";
    public static final String TOKEN_MISMATCH_EXCEPTION = "[MatGPT] 토큰의 유저 정보가 일치하지 않습니다.";

    // User
    public static final String USER_LOCALE_NOT_FOUND = "[MatGPT] 사용자에 저장된 Locale 정보가 없습니다.";
    public static final String USER_ALREADY_LOGOUT = "[MatGPT] 이미 로그아웃 된 사용자입니다.";
    public static final String USER_ALREADY_EXIST = "[MatGPT] 이미 존재하는 아이디입니다.";
    public static final String USER_NOT_FOUND = "[MatGPT] 존재하지 않는 사용자입니다.";
    public static final String LOCALE_NOT_FOUND = "[MatGPT] 존재하지 않는 로케일입니다.";

    // Coin
    public static final String COIN_USAGE_OVER_BALANCE = "[MatGPT] 잔액보다 큰 금액을 사용할 수 없습니다.";
    public static final String COIN_OUT_OF_RANGE = "[MatGPT] 최대 보유 가능 코인 범위를 벗어났습니다.";
}
