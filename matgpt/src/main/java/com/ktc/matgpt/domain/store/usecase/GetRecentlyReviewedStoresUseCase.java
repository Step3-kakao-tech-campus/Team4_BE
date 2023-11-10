package com.ktc.matgpt.domain.store.usecase;

import com.ktc.matgpt.domain.review.ReviewService;
import com.ktc.matgpt.domain.review.dto.ReviewResponse;
import com.ktc.matgpt.utils.paging.PageResponse;
import com.ktc.matgpt.utils.paging.Paging;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class GetRecentlyReviewedStoresUseCase {

    private final ReviewService reviewService;

    public PageResponse<LocalDateTime, ReviewResponse.RecentReviewDTO> execute(Long cursorId, LocalDateTime cursor) {
        cursorId = Paging.convertNullCursorToMaxValue(cursorId);
        cursor = Paging.convertNullCursorToMaxValue(cursor);
        return reviewService.getRecentlyReviewedStores(cursorId, cursor);
    }
}
