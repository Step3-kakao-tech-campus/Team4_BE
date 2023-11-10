package com.ktc.matgpt.domain.review.entity;

import com.ktc.matgpt.domain.store.Store;
import com.ktc.matgpt.domain.user.entity.User;
import com.ktc.matgpt.utils.auditing.BaseEntity;
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
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private Store store;//

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @Column(nullable = false, length = 1000)
    private String content;

    @Column(updatable = false)
    private int rating;

    @Column(nullable = false, unique = true)
    private String reviewUuid;

    @Column(updatable = false)
    private int peopleCount;

    @Column(updatable = false)
    private int totalPrice;

    @Column(updatable = false)
    private int costPerPerson;

    @Column
    private int recommendCount;

    public Review(Store store, User user, String content, int rating,
                  int peopleCount, int totalPrice) {
        this.store = store;
        this.user = user;
        this.content = content;
        this.rating = rating;
        this.reviewUuid = UUID.randomUUID().toString();
        this.peopleCount = peopleCount;
        this.totalPrice = totalPrice;
        this.costPerPerson = totalPrice / peopleCount;
    }

    public static Review create(User user, Store store, String content, int rating, int peopleCount, int totalPrice) {
        return new Review(store, user, content, rating, peopleCount, totalPrice);
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
