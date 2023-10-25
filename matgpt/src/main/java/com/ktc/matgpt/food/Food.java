package com.ktc.matgpt.food;

import com.ktc.matgpt.store.Store;
import com.ktc.matgpt.utils.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "food_tb")
public class Food extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @Column(nullable = false)
    private String foodName;

    @Column
    private String foodDescription;

    @Column
    private int reviewCount;

    @Column
    private double totalRating;

    private Food(String foodName, String foodDescription) {
        this.foodName = foodName;
        this.foodDescription = foodDescription;
    }

    @Builder
    public static Food create(String foodName, String foodDescription, Store store) {
        Food food = new Food(foodName, foodDescription);
        food.store = store;
        return food;
    }

    // 소숫점 두자리로 반환
    public double getAverageRating() {
        return reviewCount == 0 ? 0 : Math.round((totalRating / reviewCount) * 100) / 100.0;
    }

    public void addReview(double rating) {
        this.totalRating += rating;
        this.reviewCount++;
    }


    public void removeReview(double oldRating) {
        if (this.reviewCount == 0) return;
        this.totalRating -= oldRating;
        this.reviewCount--;
    }

    public void updateReview(double oldRating, double newRating) {
        this.removeReview(oldRating);
        this.addReview(newRating);
    }


}
