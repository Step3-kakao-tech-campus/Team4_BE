package com.ktc.matgpt.exception;

import com.ktc.matgpt.utils.ApiUtils;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ErrorHandlingController implements ErrorController {

    @RequestMapping("/error")
    public ResponseEntity<?> handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());
            switch (statusCode) {
                case (401) -> {
                    ApiUtils.ApiFail apiFail = ApiUtils.fail(HttpStatus.UNAUTHORIZED.value(), "로그인이 필요한 페이지입니다.");
                    return new ResponseEntity<>(apiFail, HttpStatus.UNAUTHORIZED);
                }
                case (403) -> {
                    ApiUtils.ApiFail apiFail = ApiUtils.fail(HttpStatus.FORBIDDEN.value(), "접근할 권한이 없는 페이지입니다.");
                    return new ResponseEntity<>(apiFail, HttpStatus.FORBIDDEN);
                }
                case (404) -> {
                    ApiUtils.ApiFail apiFail = ApiUtils.fail(HttpStatus.NOT_FOUND.value(), "존재하지 않는 페이지입니다.");
                    return new ResponseEntity<>(apiFail, HttpStatus.NOT_FOUND);
                }
                default -> {
                    ApiUtils.ApiFail apiFail = ApiUtils.fail(statusCode, "알 수 없는 에러가 발생했습니다.");
                    return new ResponseEntity<>(apiFail, HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
        }

        ApiUtils.ApiFail apiFail = ApiUtils.fail(HttpStatus.INTERNAL_SERVER_ERROR.value(), "내부 서버 에러가 발생했습니다.");
        return new ResponseEntity<>(apiFail, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

