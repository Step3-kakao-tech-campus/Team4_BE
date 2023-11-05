package com.ktc.matgpt.coin.repository;

import com.ktc.matgpt.coin.entity.CoinUsageHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CoinUsageHistoryRepository extends JpaRepository<CoinUsageHistory, Long> {
    @Query("SELECT cuh FROM CoinUsageHistory cuh JOIN FETCH cuh.coin c WHERE c.id = :coinId ORDER BY cuh.id DESC")
    Page<CoinUsageHistory> findAllByCoinIdOrderByHistoryIdDesc(@Param("coinId") Long coinId, Pageable pageable);

    @Query("SELECT cuh FROM CoinUsageHistory cuh JOIN FETCH cuh.coin c WHERE c.id = :coinId AND cuh.id < :cursor ORDER BY cuh.id DESC")
    Page<CoinUsageHistory> findAllByCoinIdLessThanCursor(@Param("coinId") Long coinId, @Param("cursor") Long cursor, Pageable pageable);
}
