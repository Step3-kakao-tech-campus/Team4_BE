package com.ktc.matgpt.feature_review.food;

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
public class Food {
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

    public void update(int reviewCount, double averageRating) {
        this.reviewCount = reviewCount;
        this.averageRating = averageRating;
    }
}
