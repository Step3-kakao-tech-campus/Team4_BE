package com.ktc.matgpt.domain.coin.repository;

import com.ktc.matgpt.domain.coin.entity.Coin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CoinRepository extends JpaRepository<Coin, Long> {
    Optional<Coin> findByUserId(Long userId);
}
