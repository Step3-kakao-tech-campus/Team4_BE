package com.ktc.matgpt.feature_review.review.entity;

import com.ktc.matgpt.feature_review.store.Store;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor
//@AllArgsConstructor
//@Builder
@Table(name = "review_tb")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;//

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private Store store;//

//    @Column(nullable = false)
//    private Long userId;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false, updatable = false)
    private int rating;

    @Column(nullable = false, updatable = false)
    private int peopleCount;

    @Column(nullable = false, updatable = false)
    private int totalPrice;

    @Column(nullable = false, updatable = false)
    private int costPerPerson;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Builder
    public Review(Store store, /*Long userId,*/ String content, int rating,
                  int peopleCount, int totalPrice, int costPerPerson/*, LocalDateTime relativeTime*/) {
        this.store = store;
//        this.userId = userId;
        this.content = content;
        this.rating = rating;
        this.peopleCount = peopleCount;
        this.totalPrice = totalPrice;
        this.costPerPerson = costPerPerson;
    }

    public void updateContent(String content) {
        this.content = content;
    }



}
