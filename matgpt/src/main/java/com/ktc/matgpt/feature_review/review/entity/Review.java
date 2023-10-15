package com.ktc.matgpt.feature_review.review.entity;

import com.ktc.matgpt.store.Store;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;

@Entity
@Getter
@NoArgsConstructor
//@AllArgsConstructor
//@Builder
@Table(name = "review_tb")
public class Review extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private Store store;//

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false, updatable = false)
    private double rating;

    @Column(nullable = false, updatable = false)
    private int peopleCount;

    @Column(nullable = false, updatable = false)
    private int totalPrice;

    @Column(nullable = false, updatable = false)
    private int costPerPerson;

    @Column(nullable = false)
    private int recommendCount = 0;

    @Builder
    public Review(Store store, Long userId, String content, double rating,
                  int peopleCount, int totalPrice, int costPerPerson) {
        this.store = store;
        this.userId = userId;
        this.content = content;
        this.rating = rating;
        this.peopleCount = peopleCount;
        this.totalPrice = totalPrice;
        this.costPerPerson = costPerPerson;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void plusRecommendCount() { this.recommendCount++; }
    public void minusRecommendCount() { this.recommendCount--; }
}
