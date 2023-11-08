package com.ktc.matgpt.image;

import com.ktc.matgpt.review.entity.Review;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    public Image(Review review, String url) {
        this.review = review;
        this.url = url;
    }

    @Builder
    public static Image create(Review review, String url) {
        return new Image(review, url);
    }

    public void setReview(Review review) {
        this.review = review;
    }


}
