package com.ktc.matgpt.exception;

import com.ktc.matgpt.utils.ApiUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<?> handleCustomException(CustomException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(e.body(), e.status());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException e) {
        ApiUtils.ApiFail apiFail = ApiUtils.fail(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        log.error(e.getMessage());
        return new ResponseEntity<>(apiFail, HttpStatus.BAD_REQUEST);
    }

    // Query Parameter -> Long 타입으로 변환 시, Long보다 큰 범위의 숫자를 입력했을 때 Error Handling
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<?> handleNumberFormatException(MethodArgumentTypeMismatchException e) {
        ApiUtils.ApiFail apiFail = ApiUtils.fail(HttpStatus.BAD_REQUEST.value(), "요청 타입이 잘못되었습니다.");
        log.error(String.valueOf(e.getParameter()));
        log.error(e.getName());
        log.error(e.getMessage());
        return new ResponseEntity<>(apiFail, HttpStatus.BAD_REQUEST);
    }

    // Required Query Parameter를 입력하지 않았을 경우
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<?> handleMissingParams(MissingServletRequestParameterException e) {
        String name = e.getParameterName();
        String message = String.format("'%s' 를 입력해주세요.", name);
        ApiUtils.ApiFail apiFail = ApiUtils.fail(HttpStatus.BAD_REQUEST.value(), message);
        return new ResponseEntity<>(apiFail, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> unknownServerError(Exception e){
        ApiUtils.ApiFail apiFail = ApiUtils.fail(HttpStatus.INTERNAL_SERVER_ERROR.value(), "내부 서버 오류가 발생했습니다.");
        log.error(e.getMessage());
        return new ResponseEntity<>(apiFail, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
