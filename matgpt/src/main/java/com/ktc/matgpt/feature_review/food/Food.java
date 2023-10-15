package com.ktc.matgpt.feature_review.food;

import com.ktc.matgpt.feature_review.review.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
//@AllArgsConstructor
//@Builder
@Table(name = "food_tb")
public class Food extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String foodName;

    @Column
    private String foodDescription;

    @Column
    private int reviewCount;

    @Column(nullable = false)
    private double averageRating;

    @Builder
    public Food(String foodName, int reviewCount, double averageRating) {
        this.foodName = foodName;
        this.reviewCount = reviewCount;
        this.averageRating = averageRating;
    }
    public void updatePlus(double addRating) {
        reviewCount++;
        averageRating = (averageRating * reviewCount + addRating) / reviewCount;
    }

    public void updateMinus(double subRating) {
        averageRating = (reviewCount*averageRating - subRating) / (reviewCount-1);
        reviewCount--;
    }
}
