package com.ktc.matgpt.chatgpt.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class UnixTimeConverter {

    private static final String KOREA_TIMEZONE = "Asia/Seoul"; // "GMT+9" 대신 "Asia/Seoul"을 사용

    public static LocalDateTime toLocalDateTime(int unixTimestamp) {
        return Instant.ofEpochSecond(unixTimestamp)
                .atZone(ZoneId.of(KOREA_TIMEZONE))
                .toLocalDateTime();
    }
}
