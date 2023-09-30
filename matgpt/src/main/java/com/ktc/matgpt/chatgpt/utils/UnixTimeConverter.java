package com.ktc.matgpt.chatgpt.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class UnixTimeConverter {

    private static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String KOREA_TIMEZONE = "GMT+9";

    public static String toDatetime(int unixTimestamp) {
        long timestamp = Integer.toUnsignedLong(unixTimestamp);
        Date date = new Date(timestamp * 1000L);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATETIME_FORMAT);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone(KOREA_TIMEZONE));

        return simpleDateFormat.format(date);
    }
}
