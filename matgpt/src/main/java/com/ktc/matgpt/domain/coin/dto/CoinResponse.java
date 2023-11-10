package com.ktc.matgpt.domain.coin.dto;

import com.ktc.matgpt.domain.coin.entity.CoinEarningHistory;
import com.ktc.matgpt.domain.coin.entity.CoinUsageHistory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class CoinResponse {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BalanceDto {
        private Long userId;
        private int balance;
    }


    @Getter
    @NoArgsConstructor
    public static class UsageHistoryDto {
        private int amount;
        private int balance;
        private LocalDateTime usedAt;

        public UsageHistoryDto(CoinUsageHistory coinUsageHistory) {
            this.amount = coinUsageHistory.getAmount() * -1;
            this.balance = coinUsageHistory.getBalance();
            this.usedAt = coinUsageHistory.getUsedAt();
        }
    }

    @Getter
    @NoArgsConstructor
    public static class EarningHistoryDto {
        private int amount;
        private int balance;
        private LocalDateTime earnedAt;

        public EarningHistoryDto(CoinEarningHistory coinEarningHistory) {
            this.amount = coinEarningHistory.getAmount();
            this.balance = coinEarningHistory.getBalance();
            this.earnedAt = coinEarningHistory.getEarnedAt();
        }
    }
}
