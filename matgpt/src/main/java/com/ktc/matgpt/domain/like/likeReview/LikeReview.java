package com.ktc.matgpt.domain.like.likeReview;

import com.ktc.matgpt.domain.review.entity.Review;
import com.ktc.matgpt.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "likereview_tb")
public class LikeReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="review_id")
    private Review review;


    public LikeReview(User user, Review review){
        this.user = user;
        this.review = review;
    }
    @Builder
    public static LikeReview create(User user, Review review) {
        return new LikeReview(user, review);
    }
}
