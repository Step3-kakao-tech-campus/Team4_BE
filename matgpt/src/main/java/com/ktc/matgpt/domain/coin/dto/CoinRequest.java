package com.ktc.matgpt.domain.coin.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
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
        @Positive @Max(Integer.MAX_VALUE)
        private int amount;

        public UseCoinDto(int amount) {
            this.amount = amount;
        }
    }

    @ToString
    @Getter
    @Setter
    @NoArgsConstructor
    public static class EarnCoinDto {
        @Positive @Max(Integer.MAX_VALUE)
        private int amount;

        public EarnCoinDto(int amount) {
            this.amount = amount;
        }
    }
}
