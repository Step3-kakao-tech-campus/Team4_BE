package com.ktc.matgpt.review.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum PriceRange {    // RequestDTO.CreateDTO totalPrice 에 사용 가능
    UNDER1("0~10,000"),
    OVER1_UNDER2("10,000~20,000"),
    OVER2_UNDER5("20,000~50,000"),
    OVER5_UNDER10("50,000~100,000"),
    OVER10("100,000~");


    @Getter
    private final String value;

    PriceRange(String value) {
        this.value = value;
    }

    @JsonCreator
    public static PriceRange from(String value) {
        for (PriceRange status : PriceRange.values()) {
            if (status.getValue().equals(value)) {
                return status;
            }
        }
        return null;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

}
