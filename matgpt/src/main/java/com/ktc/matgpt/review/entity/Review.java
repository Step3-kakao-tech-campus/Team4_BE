package com.ktc.matgpt.review.entity;

import com.ktc.matgpt.store.Store;
import com.ktc.matgpt.utils.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @Column(nullable = false, length = 1000)
    private String content;

    @Column(updatable = false)
    private double rating;

    @Column(nullable = false, unique = true)
    private String reviewUuid;

    @Column(updatable = false)
    private int peopleCount;

    @Column(updatable = false)
    private int totalPrice;

    @Column(updatable = false)
    private int costPerPerson;

    @Column(updatable = false)
    private int recommendCount = 0;

    public Review(Store store, Long userId, String content, double rating,
                  int peopleCount, int totalPrice) {
        this.store = store;
        this.userId = userId;
        this.content = content;
        this.rating = rating;
        this.reviewUuid = UUID.randomUUID().toString();
        this.peopleCount = peopleCount;
        this.totalPrice = totalPrice;
        this.costPerPerson = totalPrice / peopleCount;
    }

    public static Review create(Long userId, Store store, String content, double rating, int peopleCount, int totalPrice) {
        return new Review(store, userId, content, rating, peopleCount, totalPrice);
    }


    public void updateContent(String content) {
        this.content = content;
    }

    public void plusRecommendCount() {
        this.recommendCount++;
    }

    public void minusRecommendCount() {
        if (this.recommendCount > 0) {
            this.recommendCount--;
        }
    }

}
