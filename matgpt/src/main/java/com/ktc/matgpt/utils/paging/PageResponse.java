package com.ktc.matgpt.utils.paging;

import java.util.List;

public record PageResponse<T, U> (
        Paging<T> paging,
        List<U> body
){}
