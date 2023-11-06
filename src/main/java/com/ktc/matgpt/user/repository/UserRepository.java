package com.ktc.matgpt.user.repository;


import com.ktc.matgpt.user.entity.AgeGroup;
import com.ktc.matgpt.user.entity.Gender;
import com.ktc.matgpt.user.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findAllByAgeGroupAndGender(AgeGroup ageGroup, Gender gender);
    Optional<User> findByEmail(String email);
    Boolean existsByEmail(String email);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.isFirstLogin = false WHERE u.id = :userId")
    void updateFirstLoginStatus(Long userId);


}