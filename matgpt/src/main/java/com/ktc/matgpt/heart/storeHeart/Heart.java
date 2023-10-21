package com.ktc.matgpt.heart.storeHeart;

import com.ktc.matgpt.store.Store;
import com.ktc.matgpt.user.entity.User;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import static jakarta.persistence.FetchType.LAZY;


@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "heart_tb")
public class Heart {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name="user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name="store_id")
    private Store store;
}
