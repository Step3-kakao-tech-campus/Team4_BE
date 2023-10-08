package com.ktc.matgpt.feature_review.image;

import com.ktc.matgpt.feature_review.review.entity.Review;
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
@Table(name = "image_tb")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private Review review;

    @Column(nullable = false)
    private String url;

    @Builder
    public Image(Review review, String url) {
        this.review = review;
        this.url = url;
    }
}
