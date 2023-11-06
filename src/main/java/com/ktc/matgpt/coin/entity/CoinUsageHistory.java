package com.ktc.matgpt.coin.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
@Table(name = "coin_usage_history_tb")
@Entity
public class CoinUsageHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Coin coin;

    private int balance;

    private int amount;

    private LocalDateTime usedAt;

    public CoinUsageHistory(Coin coin, int amount) {
        this.coin = coin;
        this.balance = coin.getBalance();
        this.amount = amount;
        this.usedAt = LocalDateTime.now();
    }

    public static CoinUsageHistory create(Coin coin, int amount) {
        return new CoinUsageHistory(coin, amount);
    }
}
