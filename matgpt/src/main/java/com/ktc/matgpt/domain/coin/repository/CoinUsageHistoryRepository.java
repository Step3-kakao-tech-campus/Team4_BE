package com.ktc.matgpt.domain.coin.repository;

import com.ktc.matgpt.domain.coin.entity.CoinUsageHistory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface CoinUsageHistoryRepository extends JpaRepository<CoinUsageHistory, Long> {
    @Query("SELECT cuh FROM CoinUsageHistory cuh " +
            "JOIN FETCH cuh.coin c " +
            "WHERE c.id = :coinId AND cuh.usedAt < :cursor OR (cuh.usedAt = :cursor AND cuh.id < :cursorId) " +
            "ORDER BY cuh.usedAt DESC, cuh.id DESC")
    List<CoinUsageHistory> findAllByCoinIdLessThanCursor(@Param("coinId") Long coinId, @Param("cursor") LocalDateTime cursor, @Param("cursorId") Long cursorId, Pageable pageable);
}
