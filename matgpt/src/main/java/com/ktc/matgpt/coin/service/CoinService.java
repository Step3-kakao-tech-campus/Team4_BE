package com.ktc.matgpt.coin.service;

import com.ktc.matgpt.coin.dto.CoinRequest;
import com.ktc.matgpt.coin.dto.CoinResponse;
import com.ktc.matgpt.coin.entity.Coin;
import com.ktc.matgpt.coin.entity.CoinEarningHistory;
import com.ktc.matgpt.coin.entity.CoinUsageHistory;
import com.ktc.matgpt.coin.repository.CoinEarningHistoryRepository;
import com.ktc.matgpt.coin.repository.CoinRepository;
import com.ktc.matgpt.coin.repository.CoinUsageHistoryRepository;
import com.ktc.matgpt.user.entity.User;
import com.ktc.matgpt.utils.PageResponse;
import com.ktc.matgpt.utils.Paging;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class CoinService {

    private final CoinRepository coinRepository;
    private final CoinUsageHistoryRepository coinUsageHistoryRepository;
    private final CoinEarningHistoryRepository coinEarningHistoryRepository;
    private static final int PAGE_SIZE_PLUS_ONE = 10 + 1;

    @Transactional(readOnly = true)
    public CoinResponse.BalanceDto getCoinBalance(Long userId) {
        Coin coin = getCoinByUserId(userId);
        return new CoinResponse.BalanceDto(userId, coin.getBalance());
    }

    @Transactional
    public CoinResponse.BalanceDto useCoin(Long userId, CoinRequest.UseCoinDto useCoinDto) {
        Coin coin = getCoinByUserId(userId);
        int balance = coin.getBalance();
        int amount = useCoinDto.getAmount();

        if (balance < amount) {
            throw new IllegalArgumentException("잔액보다 큰 금액을 사용할 수 없습니다.");
        }

        coin.use(amount);
        CoinUsageHistory coinUsageHistory = CoinUsageHistory.create(coin, amount);
        coinUsageHistoryRepository.save(coinUsageHistory);

        return new CoinResponse.BalanceDto(userId, coin.getBalance());
    }

    @Transactional
    public CoinResponse.BalanceDto earnCoin(Long userId, CoinRequest.EarnCoinDto earnCoinDto) {
        Coin coin = getCoinByUserId(userId);
        int amount = earnCoinDto.getAmount();

        coin.earn(amount);
        CoinEarningHistory coinEarningHistory = CoinEarningHistory.create(coin, amount);
        coinEarningHistoryRepository.save(coinEarningHistory);

        return new CoinResponse.BalanceDto(userId, coin.getBalance());
    }

    @Transactional
    public void createCoin(User user) {
        Coin coin = Coin.create(user);
        coinRepository.save(coin);
    }

    @Transactional(readOnly = true)
    public PageResponse<?, CoinResponse.EarningHistoryDto> getCoinEarningHistory(Long userId, LocalDateTime cursor, Long cursorId) {
        Pageable pageable = PageRequest.ofSize(PAGE_SIZE_PLUS_ONE);
        Coin coin = getCoinByUserId(userId);
        cursor = Paging.convertNullCursorToMaxValue(cursor);
        List<CoinEarningHistory> coinEarningHistories = coinEarningHistoryRepository.findAllByCoinIdLessThanCursor(coin.getId(), cursor, cursorId, pageable);

        if (coinEarningHistories.isEmpty()) {
            return new PageResponse<>(new Paging<>(false, 0, null, null), null);
        }

        boolean hasNext = false;
        int size = coinEarningHistories.size();
        if (size == PAGE_SIZE_PLUS_ONE) {
            coinEarningHistories.remove(size - 1);
            hasNext = true;
            size -= 1;
        }

        Long nextCursor = coinEarningHistories.get(size - 1).getId();
        Paging<Long> paging = new Paging<>(hasNext, size, nextCursor, nextCursor);
        return new PageResponse<>(paging, coinEarningHistories.stream()
                .map(CoinResponse.EarningHistoryDto::new)
                .toList());
    }

    @Transactional(readOnly = true)
    public PageResponse<?, CoinResponse.UsageHistoryDto> getCoinUsageHistory(Long userId, LocalDateTime cursor, Long cursorId) {
        Pageable pageable = PageRequest.ofSize(PAGE_SIZE_PLUS_ONE);
        Coin coin = getCoinByUserId(userId);
        cursor = Paging.convertNullCursorToMaxValue(cursor);
        List<CoinUsageHistory> coinUsageHistories = coinUsageHistoryRepository.findAllByCoinIdLessThanCursor(coin.getId(), cursor, cursorId, pageable);

        if (coinUsageHistories.isEmpty()) {
            return new PageResponse<>(new Paging<>(false, 0, null, null), null);
        }

        boolean hasNext = false;
        int size = coinUsageHistories.size();
        if (size == PAGE_SIZE_PLUS_ONE) {
            coinUsageHistories.remove(size - 1);
            hasNext = true;
            size -= 1;
        }

        Long nextCursor = coinUsageHistories.get(size - 1).getId();
        Paging<Long> paging = new Paging<>(hasNext, size, nextCursor, nextCursor);
        return new PageResponse<>(paging, coinUsageHistories.stream()
                .map(CoinResponse.UsageHistoryDto::new)
                .toList());
    }

    private Coin getCoinByUserId(Long userId) {
        return coinRepository.findByUserId(userId).orElseThrow(
                () -> new NoSuchElementException(String.format("userId-%s : 존재하지 않는 유저id입니다.", userId)));
    }
}
