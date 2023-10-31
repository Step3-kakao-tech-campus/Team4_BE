package com.ktc.matgpt.like.likeReview;

import com.ktc.matgpt.like.likeStore.LikeStore;
import com.ktc.matgpt.review.entity.Review;
import com.ktc.matgpt.store.Store;
import com.ktc.matgpt.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;

public class LikeReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="store_id")
    private Store store;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="review_id")
    private Review review;


    public LikeReview(User user, Store store, Review review){
        this.user = user;
        this.store = store;
        this.review = review;
    }
    @Builder
    public static LikeReview create(User user, Store store, Review review) {
        return new LikeReview(user, store, review);
    }
}
