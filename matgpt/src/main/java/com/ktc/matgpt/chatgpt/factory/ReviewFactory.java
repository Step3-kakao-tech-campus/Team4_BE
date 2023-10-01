package com.ktc.matgpt.chatgpt.factory;

import java.util.Comparator;
import java.util.List;

public class ReviewFactory {

    private static final List<Review> reviews = List.of(
            new Review(1L, "직원들이 매우 친절해요.", 4.3),
            new Review(2L, "음식이 매우 맛있어요. 특히 새우볶음밥이 맛나네요", 4.5),
            new Review(3L, "대체로 분위기가 좋고 음식도 맛있었네요~", 4.7),
            new Review(4L, "직원들 친절하고 맛있어요. 조금 매콤할수는 있어요.", 4.1),
            new Review(5L, "가격이 합리적이고 가성비가 좋아요", 4.2)
    );

    public static List<Review> getTopReviews(int n) {
        return reviews.stream()
                .sorted(Comparator.comparing(Review::rating).reversed())
                .limit(n)
                .toList();
    }
}
