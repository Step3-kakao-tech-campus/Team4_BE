package com.ktc.matgpt.store.usecase;

import com.ktc.matgpt.review.ReviewService;
import com.ktc.matgpt.review.dto.ReviewResponse;
import com.ktc.matgpt.utils.PageResponse;
import com.ktc.matgpt.utils.Paging;
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
