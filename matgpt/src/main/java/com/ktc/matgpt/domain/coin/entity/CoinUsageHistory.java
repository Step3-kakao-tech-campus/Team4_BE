package com.ktc.matgpt.domain.coin.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
@Table(name = "coin_usage_history_tb")
@Entity
public class CoinUsageHistory extends CoinHistory {

    private LocalDateTime usedAt;

    public CoinUsageHistory(Coin coin, int amount) {
        super(coin, amount);
        this.usedAt = LocalDateTime.now();
    }

    @Override
    public LocalDateTime getTransactionTime() {
        return this.usedAt;
    }

    public static CoinUsageHistory create(Coin coin, int amount) {
        return new CoinUsageHistory(coin, amount);
    }
}