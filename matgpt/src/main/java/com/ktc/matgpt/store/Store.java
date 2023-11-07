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
    private int avgCostPerPerson;

    @Column
    private double avgVisitCount;


    @Column(nullable = false)
    private int numsOfReview;

    @Column(nullable = false)
    private double avgRating;

    public void addReview(int rating, int visitCount, int costPerPerson) {
        this.avgRating = (this.avgRating * this.numsOfReview + rating) / (this.numsOfReview + 1);
        this.avgVisitCount = (this.avgVisitCount * this.numsOfReview + visitCount) / (this.numsOfReview + 1);
        this.avgCostPerPerson = (this.avgCostPerPerson * this.numsOfReview + costPerPerson) / (this.numsOfReview + 1);
        this.numsOfReview++;
    }

    public void removeReview(int rating, int visitCount, int costPerPerson) {
        if (this.numsOfReview == 1) {
            this.avgRating = 0;
            this.avgVisitCount = 0;
            this.numsOfReview--;
            this.avgCostPerPerson = 0;
            return;
        }
        this.avgRating = (this.avgRating * this.numsOfReview - rating) / (this.numsOfReview - 1);
        this.avgVisitCount = (this.avgVisitCount * this.numsOfReview - visitCount) / (this.numsOfReview - 1);
        this.avgRating = (this.avgRating * this.numsOfReview - rating) / (this.numsOfReview - 1);
        this.avgCostPerPerson = (this.avgCostPerPerson * this.numsOfReview - costPerPerson) / (this.numsOfReview - 1);
        this.numsOfReview--;
    }

    // Store 평점을 소수점 2번째까지만 나타냄(반올림)
    public double getAverageRating() {
        return Math.round(avgRating * 100) / 100.00;
    }

    // Store 평균 방문인원을 소수점 1번째까지만 나타냄(반올림)
    public int getAverageVisitCount() {
        return (int) avgVisitCount;
    }

    public int getAvgCostPerPerson() {
        return avgCostPerPerson;
    }

    public double calculateDistanceFromLatLon(Double latitude, Double longitude) {
        final double EARTH_RADIUS = 6371.0;
        double latDistance = Math.toRadians(latitude - this.latitude);
        double lonDistance = Math.toRadians(longitude - this.longitude);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(this.latitude)) * Math.cos(Math.toRadians(latitude))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS * c; // 결과는 킬로미터 단위로 반환.
    }
}
