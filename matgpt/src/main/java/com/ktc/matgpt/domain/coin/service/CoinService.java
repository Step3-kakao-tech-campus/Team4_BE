package com.ktc.matgpt.domain.coin.service;

import com.ktc.matgpt.domain.coin.dto.CoinRequest;
import com.ktc.matgpt.domain.coin.dto.CoinResponse;
import com.ktc.matgpt.domain.coin.entity.Coin;
import com.ktc.matgpt.domain.coin.entity.CoinEarningHistory;
import com.ktc.matgpt.domain.coin.entity.CoinUsageHistory;
import com.ktc.matgpt.domain.coin.repository.CoinRepository;
import com.ktc.matgpt.domain.user.entity.User;
import com.ktc.matgpt.exception.ErrorMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class CoinService {

    private final CoinHistoryService coinHistoryService;
    private final CoinRepository coinRepository;

    @Transactional(readOnly = true)
    public CoinResponse.BalanceDto getCoinBalance(Long userId) {
        Coin coin = getCoinByUserId(userId);
        return new CoinResponse.BalanceDto(userId, coin.getBalance());
    }

    @Transactional
    public void createCoin(User user) {
        Coin coin = Coin.create(user);
        coinRepository.save(coin);
    }

    @Transactional
    public CoinResponse.BalanceDto earnCoin(Long userId, CoinRequest.EarnCoinDto earnCoinDto) {
        Coin coin = getCoinByUserId(userId);
        int amount = earnCoinDto.getAmount();

        coin.earn(amount);
        CoinEarningHistory coinEarningHistory = CoinEarningHistory.create(coin, amount);
        coinHistoryService.save(coinEarningHistory);

        return new CoinResponse.BalanceDto(userId, coin.getBalance());
    }

    @Transactional
    public CoinResponse.BalanceDto useCoin(Long userId, CoinRequest.UseCoinDto useCoinDto) {
        Coin coin = getCoinByUserId(userId);
        int amount = useCoinDto.getAmount();

        coin.use(amount);
        CoinUsageHistory coinUsageHistory = CoinUsageHistory.create(coin, amount);
        coinHistoryService.save(coinUsageHistory);

        return new CoinResponse.BalanceDto(userId, coin.getBalance());
    }

    private Coin getCoinByUserId(Long userId) {
        return coinRepository.findByUserId(userId).orElseThrow(
                () -> new NoSuchElementException(ErrorMessage.USER_NOT_FOUND));
    }
}
