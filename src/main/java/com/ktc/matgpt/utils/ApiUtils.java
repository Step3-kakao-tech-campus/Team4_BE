package com.ktc.matgpt.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class ApiUtils {

    public static <T> ApiSuccess<T> success(T response) {
        return new ApiSuccess<>(response);
    }

    public static ApiFail fail(int errorCode, String message) {
        return new ApiFail(errorCode, message);
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class ApiSuccess<T> {
        private final T data;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class ApiFail {
        // TODO: define body format
        private final int errorCode;
        private final String message;
    }

}