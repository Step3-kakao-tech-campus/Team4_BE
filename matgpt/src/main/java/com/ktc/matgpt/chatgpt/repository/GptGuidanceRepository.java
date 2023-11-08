package com.ktc.matgpt.chatgpt.repository;

import com.ktc.matgpt.chatgpt.entity.GptGuidance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GptGuidanceRepository extends JpaRepository<GptGuidance, Long> {
    List<GptGuidance> findAllByUserId(Long userId);
}
