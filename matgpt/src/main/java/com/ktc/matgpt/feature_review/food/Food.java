package com.ktc.matgpt.feature_review.food;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

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

    @Column(nullable = false)
    private double averageRating;

    @Builder
    public Food(String foodName, double averageRating) {
        this.foodName = foodName;
        this.averageRating = averageRating;
    }
}
