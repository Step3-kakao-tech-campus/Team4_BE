package com.ktc.matgpt.feature_review.food;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FoodJPARepository extends JpaRepository<Food, Long> {
    Optional<Food> findByFoodName(String foodName);
}
