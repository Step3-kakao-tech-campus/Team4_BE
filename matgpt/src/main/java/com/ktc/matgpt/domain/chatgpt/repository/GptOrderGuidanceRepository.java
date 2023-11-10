package com.ktc.matgpt.domain.chatgpt.repository;

import com.ktc.matgpt.domain.chatgpt.entity.GptOrderGuidance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GptOrderGuidanceRepository extends JpaRepository<GptOrderGuidance, Long> {
    List<GptOrderGuidance> findAllByUserId(Long userId);
}
