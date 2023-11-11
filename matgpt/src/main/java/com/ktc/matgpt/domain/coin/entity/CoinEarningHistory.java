package com.ktc.matgpt.domain.coin.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
@Table(name = "coin_earning_history_tb")
@Entity
public class CoinEarningHistory extends CoinHistory {

    private LocalDateTime earnedAt;

    public CoinEarningHistory(Coin coin, int amount) {
        super(coin, amount);
        this.earnedAt = LocalDateTime.now();
    }

    @Override
    public LocalDateTime getTransactionTime() {
        return this.earnedAt;
    }

    public static CoinEarningHistory create(Coin coin, int amount) {
        return new CoinEarningHistory(coin, amount);
    }
}
