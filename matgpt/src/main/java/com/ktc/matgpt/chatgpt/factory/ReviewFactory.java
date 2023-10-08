package com.ktc.matgpt.chatgpt.factory;

import java.util.Comparator;
import java.util.List;

public class ReviewFactory {

    private static final List<MockReview> MOCK_REVIEWS = List.of(
            new MockReview(1L, "직원들이 매우 친절해요.", 4.3),
            new MockReview(2L, "음식이 매우 맛있어요. 특히 새우볶음밥이 맛나네요", 4.5),
            new MockReview(3L, "대체로 분위기가 좋고 음식도 맛있었네요~", 4.7),
            new MockReview(4L, "직원들 친절하고 맛있어요. 조금 매콤할수는 있어요.", 4.1),
            new MockReview(5L, "가격이 합리적이고 가성비가 좋아요", 4.2)
    );

    public static List<MockReview> getTopReviews(int n) {
        return MOCK_REVIEWS.stream()
                .sorted(Comparator.comparing(MockReview::rating).reversed())
                .limit(n)
                .toList();
    }
}
