package com.ktc.matgpt.domain.coin.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
public abstract class CoinHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Coin coin;

    private int balance;

    private int amount;

    protected CoinHistory(Coin coin, int amount) {
        this.coin = coin;
        this.balance = coin.getBalance();
        this.amount = amount;
    }

    protected CoinHistory() {}

    public abstract LocalDateTime getTransactionTime();

}

