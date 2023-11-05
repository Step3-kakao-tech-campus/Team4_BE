package com.ktc.matgpt.utils;

import java.time.LocalDateTime;

public record Paging<T> (
        boolean hasNext,
        int size,
        T nextCursor,
        Long nextCursorId
) {

    public static Double convertNullCursorToMinValue(Double cursor) {
        return cursor != null ? cursor : Double.MIN_VALUE;
    }

    public static Integer convertNullCursorToMaxValue(Integer cursor) {
        return cursor != null ? cursor : Integer.MAX_VALUE;
    }

    public static Long convertNullCursorToMaxValue(Long cursor) {
        return cursor != null ? cursor : Long.MAX_VALUE;
    }

    public static LocalDateTime convertNullCursorToMaxValue(LocalDateTime cursor) {
        return cursor != null ? cursor : LocalDateTime.now().plusYears(1000);
    }
}
