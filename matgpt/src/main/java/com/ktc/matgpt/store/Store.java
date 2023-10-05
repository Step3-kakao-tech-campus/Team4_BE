package com.ktc.matgpt.store;


import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.DateFormat;
import java.text.DecimalFormat;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "store")
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String storeName;

    @Column(nullable = false)
    private DecimalFormat avgCostPerPerson;

    @Column(nullable = false)
    private String avgVisitCount;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private String openingTime;
    @Column(nullable = false)
    private String closingTime;

    @Column(nullable = true)
    private String storeImg;

    @Column(nullable = false)
    private Long latitude;

    @Column(nullable = false)
    private Long longtitude;

    @Column(nullable = false)
    private int numsOfReview;

    @Column(nullable = false)
    private double ratingAvg;


}
