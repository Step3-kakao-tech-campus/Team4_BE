package com.ktc.matgpt.feature_review.tag;

import com.ktc.matgpt.feature_review.food.Food;
import com.ktc.matgpt.feature_review.image.Image;
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
@Table(name = "tag_tb")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private Image image;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private Food food;

    @Column(nullable = false)
    private String menu_name;

    @Column
    private double menu_rating;

    @Column
    private int location_x;

    @Column
    private int location_y;

    @Builder
    public Tag(Image image, Food food, String menu_name, double menu_rating, int location_x, int location_y) {
        this.image = image;
        this.food = food;
        this.menu_name = menu_name;
        this.menu_rating = menu_rating;
        this.location_x = location_x;
        this.location_y = location_y;
    }
}
