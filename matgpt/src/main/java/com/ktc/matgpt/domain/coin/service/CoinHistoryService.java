package com.ktc.matgpt.domain.coin.service;

import com.ktc.matgpt.domain.coin.dto.CoinResponse;
import com.ktc.matgpt.domain.coin.entity.CoinEarningHistory;
import com.ktc.matgpt.domain.coin.entity.CoinHistory;
import com.ktc.matgpt.domain.coin.entity.CoinUsageHistory;
import com.ktc.matgpt.domain.coin.repository.CoinEarningHistoryRepository;
import com.ktc.matgpt.domain.coin.repository.CoinUsageHistoryRepository;
import com.ktc.matgpt.utils.paging.PageResponse;
import com.ktc.matgpt.utils.paging.Paging;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CoinHistoryService {

    private final CoinEarningHistoryRepository coinEarningHistoryRepository;
    private final CoinUsageHistoryRepository coinUsageHistoryRepository;

    private static final int PAGE_SIZE_PLUS_ONE = 10 + 1;
    private static final Pageable PAGEABLE_DEFAULT = PageRequest.ofSize(PAGE_SIZE_PLUS_ONE);

    @Transactional(readOnly = true)
    public PageResponse<?, CoinResponse.EarningHistoryDto> getCoinEarningHistory(Long userId, LocalDateTime cursor, Long cursorId) {
        cursor = Paging.convertNullCursorToMaxValue(cursor);
        List<CoinEarningHistory> coinEarningHistories = coinEarningHistoryRepository.findAllByCoinIdLessThanCursor(userId, cursor, cursorId, PAGEABLE_DEFAULT);

        Paging<LocalDateTime> paging = createPaging(coinEarningHistories);
        return new PageResponse<>(paging, coinEarningHistories.stream()
                .map(CoinResponse.EarningHistoryDto::new)
                .toList());
    }

    @Transactional(readOnly = true)
    public PageResponse<?, CoinResponse.UsageHistoryDto> getCoinUsageHistory(Long userId, LocalDateTime cursor, Long cursorId) {
        cursor = Paging.convertNullCursorToMaxValue(cursor);
        List<CoinUsageHistory> coinUsageHistories = coinUsageHistoryRepository.findAllByCoinIdLessThanCursor(userId, cursor, cursorId, PAGEABLE_DEFAULT);

        Paging<LocalDateTime> paging = createPaging(coinUsageHistories);
        return new PageResponse<>(paging, coinUsageHistories.stream()
                .map(CoinResponse.UsageHistoryDto::new)
                .toList());
    }

    @Transactional
    public void save(CoinEarningHistory coinEarningHistory) {
        coinEarningHistoryRepository.save(coinEarningHistory);
    }

    @Transactional
    public void save(CoinUsageHistory coinUsageHistory) {
        coinUsageHistoryRepository.save(coinUsageHistory);
    }

    private static Paging<LocalDateTime> createPaging(List<? extends CoinHistory> coinHistories) {
        if (coinHistories.isEmpty()) {
            return new Paging<>(false, 0, null, null);
        }
        boolean hasNext = false;
        int size = coinHistories.size();
        if (size == PAGE_SIZE_PLUS_ONE) {
            coinHistories.remove(size - 1);
            hasNext = true;
            size -= 1;
        }

        CoinHistory lastHistory = coinHistories.get(size - 1);
        return new Paging<>(hasNext, size, lastHistory.getTransactionTime(), lastHistory.getId());
    }
}
