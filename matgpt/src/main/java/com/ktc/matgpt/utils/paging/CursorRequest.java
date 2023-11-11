package com.ktc.matgpt.utils.paging;

import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;

public class CursorRequest<T> {
    public PageRequest request;
    public T cursor;
    public Long cursorId;

    public CursorRequest(int size, T cursor, Class<T> clazz, Long cursorId) {
        this.request = PageRequest.ofSize(size);
        this.cursor = convertNullCursorToMaxValue(cursor, clazz);
        this.cursorId = Paging.convertNullCursorToMaxValue(cursorId);
    }

    private static <T> T convertNullCursorToMaxValue(T cursor, Class<T> clazz) {
        if (clazz == Double.class) {
            return (T) Paging.convertNullCursorToMinValue((Double) cursor);
        } else if (clazz == Integer.class) {
            return (T) Paging.convertNullCursorToMaxValue((Integer) cursor);
        } else if (clazz == Long.class) {
            return (T) Paging.convertNullCursorToMaxValue((Long) cursor);
        } else if (clazz == LocalDateTime.class) {
            return (T) Paging.convertNullCursorToMaxValue((LocalDateTime) cursor);
        }
        return null;
    }
}