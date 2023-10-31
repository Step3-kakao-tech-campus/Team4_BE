package com.ktc.matgpt.utils;

import java.util.List;

public record PageResponse<T, U> (
        CursorRequest<T> cursorRequest,
        List<U> body
){}
