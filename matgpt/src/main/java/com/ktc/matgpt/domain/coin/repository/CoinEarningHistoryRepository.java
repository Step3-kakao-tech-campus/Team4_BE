package com.ktc.matgpt.domain.coin.repository;

import com.ktc.matgpt.domain.coin.entity.CoinEarningHistory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface CoinEarningHistoryRepository extends JpaRepository<CoinEarningHistory, Long> {
    @Query("SELECT ceh FROM CoinEarningHistory ceh " +
            "JOIN FETCH ceh.coin c " +
            "WHERE c.user.id = :userId " +
            "AND ceh.earnedAt < :cursor OR (ceh.earnedAt = :cursor AND ceh.id < :cursorId)" +
            "ORDER BY ceh.earnedAt DESC, ceh.id DESC")
    List<CoinEarningHistory> findAllByUserIdLessThanCursor(@Param("userId") Long userId, @Param("cursor") LocalDateTime cursor, @Param("cursorId")Long cursorId, Pageable pageable);
}
