package com.ktc.matgpt.coin.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
@Table(name = "coin_earning_history_tb")
@Entity
public class CoinEarningHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Coin coin;

    private int balance;

    private int amount;

    private LocalDateTime earnedAt;

    public CoinEarningHistory(Coin coin, int amount) {
        this.coin = coin;
        this.balance = coin.getBalance();
        this.amount = amount;
        this.earnedAt = LocalDateTime.now();
    }

    public static CoinEarningHistory create(Coin coin, int amount) {
        return new CoinEarningHistory(coin, amount);
    }
}
