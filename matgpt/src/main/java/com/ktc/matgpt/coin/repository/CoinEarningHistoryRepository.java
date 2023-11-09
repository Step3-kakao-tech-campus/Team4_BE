package com.ktc.matgpt.coin.repository;

import com.ktc.matgpt.coin.entity.CoinEarningHistory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface CoinEarningHistoryRepository extends JpaRepository<CoinEarningHistory, Long> {
    @Query("SELECT ceh FROM CoinEarningHistory ceh " +
            "JOIN FETCH ceh.coin c " +
            "WHERE c.id = :coinId " +
            "AND ceh.earnedAt < :cursor OR (ceh.earnedAt = :cursor AND ceh.id < :cursorId)" +
            "ORDER BY ceh.earnedAt DESC, ceh.id DESC")
    List<CoinEarningHistory> findAllByCoinIdLessThanCursor(@Param("coinId") Long coinId, @Param("cursor") LocalDateTime cursor, @Param("cursorId")Long cursorId, Pageable pageable);
}
