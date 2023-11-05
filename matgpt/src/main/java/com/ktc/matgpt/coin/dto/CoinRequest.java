package com.ktc.matgpt.coin.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

public class CoinRequest {

    @ToString
    @Getter
    @Setter
    @NoArgsConstructor
    public static class UseCoinDto {
        private int amount;

        public UseCoinDto(int amount) {
            validateAmount(amount);
            this.amount = amount;
        }
    }

    @ToString
    @Getter
    @Setter
    @NoArgsConstructor
    public static class EarnCoinDto {
        private int amount;

        public EarnCoinDto(int amount) {
            validateAmount(amount);
            this.amount = amount;
        }
    }

    public static void validateAmount(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("amount는 음수가 될 수 없습니다.");
        }
    }
}
