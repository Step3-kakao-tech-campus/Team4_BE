package com.ktc.matgpt.utils;

public record CursorRequest<T> (
        T nextCursor,
        Long lastId,
        int size
) {}
