package com.ktc.matgpt.coin.entity;

import com.ktc.matgpt.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Table(name = "coin_tb")
@Entity
public class Coin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private int balance;

    public Coin(User user) {
        this.user = user;
        this.balance = 0;
    }

    public static Coin create(User user) {
        return new Coin(user);
    }

    public void use(int amount) {
        this.balance -= amount;
    }

    public void earn(int amount) {
        this.balance += amount;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }
}
