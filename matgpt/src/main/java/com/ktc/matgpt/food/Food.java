package com.ktc.matgpt.food;

import com.ktc.matgpt.store.Store;
import com.ktc.matgpt.utils.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "food_tb")
public class Food extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @Column(nullable = false)
    private String foodName;


    @Column
    private int numsOfReview;

    @Column
    private double avgRating;

    private Food(String foodName, int firstRating) {
        this.foodName = foodName;
        this.numsOfReview = 1;
        this.avgRating = firstRating;

    }

    @Builder
    public static Food create(String foodName, int firstRating, Store store) {
        Food food = new Food(foodName, firstRating);
        food.store = store;
        return food;
    }

    // 소숫점 두자리로 반환
    public double getAverageRating() {
        return numsOfReview == 0 ? 0 : Math.round(avgRating * 100) / 100.0;
    }

    public void addReview(int rating) {
        this.avgRating = (this.avgRating * this.numsOfReview + rating) / (this.numsOfReview + 1);
        this.numsOfReview++;
    }


    public void removeReview(int oldRating) {
        if (this.numsOfReview == 1) {
            this.avgRating = 0;
            this.numsOfReview--;
            return;
        }
        this.avgRating = (this.avgRating * this.numsOfReview - oldRating) / (this.numsOfReview - 1);
        this.numsOfReview--;
    }

//    public void updateReview(double oldRating, double newRating) {
//        this.removeReview(oldRating);
//        this.addReview(newRating);
//    }


}
