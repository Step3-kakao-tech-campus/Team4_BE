package com.ktc.matgpt.chatgpt.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.ktc.matgpt.chatgpt.exception.ApiException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public enum FinishReason {
    STOP("stop"),
    LENGTH("length"),
    FUNCTION_CALL("function_call"),
    CONTENT_FILTER("content_filter"),
    NULL("null"),
    TIMEOUT("timeout");

    private final String value;

    FinishReason(String value) {
        this.value = value;
    }

    @JsonCreator
    public static FinishReason forValue(String value) {
        for (FinishReason finishReason : values()) {
            if (value.equals(finishReason.getValue())) {
                return finishReason;
            }
        }

        log.error("[ChatGPT API] UnknownFinishReasonException : " + value);
        throw new ApiException.UnknownFinishReasonException();
    }
}
