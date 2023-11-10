package com.ktc.matgpt.domain.user.repository;


import com.ktc.matgpt.domain.user.entity.AgeGroup;
import com.ktc.matgpt.domain.user.entity.Gender;
import com.ktc.matgpt.domain.user.entity.User;
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
    boolean existsByEmail(String email);

}