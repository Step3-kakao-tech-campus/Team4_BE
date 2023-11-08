package com.ktc.matgpt.tag;

import com.ktc.matgpt.food.Food;
import com.ktc.matgpt.image.Image;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "tag_tb")
@Entity
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

    @Column
    private String tagName;

    @Column
    private int menuRating;

    @Column
    private double locationX;

    @Column
    private double locationY;

    public Tag(Image image, Food food, String tagName, int menuRating, double locationX, double locationY) {
        this.image = image;
        this.food = food;
        this.tagName = tagName;
        this.menuRating = menuRating;
        this.locationX = locationX;
        this.locationY = locationY;
    }

    public static Tag create(Image image, Food food, String tagName, int menuRating, double locationX, double locationY) {
        return new Tag(image, food, tagName, menuRating, locationX, locationY);
    }


}
