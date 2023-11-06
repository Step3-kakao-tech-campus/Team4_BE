package com.ktc.matgpt.utils;

import java.util.List;

public record PageResponse<T, U> (
        Paging<T> paging,
        List<U> body
){}
