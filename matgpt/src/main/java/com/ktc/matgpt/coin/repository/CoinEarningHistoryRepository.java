package com.ktc.matgpt.coin.repository;

import com.ktc.matgpt.coin.entity.CoinEarningHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CoinEarningHistoryRepository extends JpaRepository<CoinEarningHistory, Long> {
    @Query("SELECT ceh FROM CoinEarningHistory ceh JOIN FETCH ceh.coin c WHERE c.id = :coinId ORDER BY ceh.id DESC")
    Page<CoinEarningHistory> findAllByCoinIdOrderByHistoryIdDesc(@Param("coinId") Long coinId, Pageable pageable);

    @Query("SELECT ceh FROM CoinEarningHistory ceh JOIN FETCH ceh.coin c WHERE c.id = :coinId AND ceh.id < :cursor ORDER BY ceh.id DESC")
    Page<CoinEarningHistory> findAllByCoinIdLessThanCursor(@Param("coinId") Long coinId, @Param("cursor") Long cursor, Pageable pageable);
}
