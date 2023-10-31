package com.ktc.matgpt.store;


import com.ktc.matgpt.store.entity.SubCategory;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "store_tb")
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_category_id")
    private SubCategory subCategory;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String businessHours;

    @Column
    private String storeImageUrl;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @Column
    private double avgCostPerPerson;

    @Column
    private int avgVisitCount;


    @Column(nullable = false)
    private int numsOfReview;

    @Column(nullable = false)
    private double ratingAvg;

    public void addReview(double rating) {
        this.ratingAvg = (this.ratingAvg * this.numsOfReview + rating) / (this.numsOfReview + 1);
        this.numsOfReview++;
    }

    public void removeReview(double rating) {
        if (this.numsOfReview == 1) {
            this.ratingAvg = 0;
            this.numsOfReview--;
            return;
        }
        this.ratingAvg = (this.ratingAvg * this.numsOfReview - rating) / (this.numsOfReview - 1);
        this.numsOfReview--;
    }


}
