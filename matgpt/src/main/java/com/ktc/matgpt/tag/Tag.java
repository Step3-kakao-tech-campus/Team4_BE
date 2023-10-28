package com.ktc.matgpt.tag;

import com.ktc.matgpt.food.Food;
import com.ktc.matgpt.image.Image;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @Column
    private double menuRating;

    @Column
    private int locationX;

    @Column
    private int locationY;

    public Tag(Image image, Food food, double menuRating, int locationX, int locationY) {
        this.image = image;
        this.food = food;
        this.menuRating = menuRating;
        this.locationX = locationX;
        this.locationY = locationY;
    }

    public static Tag create(Image image, Food food, double menuRating, int locationX, int locationY) {
        return new Tag(image, food, menuRating, locationX, locationY);
    }


}
