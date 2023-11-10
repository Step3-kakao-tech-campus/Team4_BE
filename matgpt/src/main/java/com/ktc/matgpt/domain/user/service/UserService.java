package com.ktc.matgpt.domain.user.service;

import com.ktc.matgpt.domain.user.entity.AgeGroup;
import com.ktc.matgpt.domain.user.entity.Gender;
import com.ktc.matgpt.domain.user.entity.User;
import com.ktc.matgpt.domain.user.repository.UserRepository;
import com.ktc.matgpt.exception.ErrorMessage;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final EntityManager entityManager;

    public boolean existsById(Long id) {
        return userRepository.existsById(id);
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException(ErrorMessage.USER_NOT_FOUND));
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(
                () -> new NoSuchElementException(ErrorMessage.USER_NOT_FOUND));
    }

    public User getReferenceByEmail(String email) {
        // 실제 엔터티를 로드하지 않고, 프록시 객체를 반환
        return entityManager.getReference(User.class, findByEmail(email).getId());
    }

    public List<User> findByAgeGroupAndGender(AgeGroup ageGroup, Gender gender) {
        return userRepository.findAllByAgeGroupAndGender(ageGroup, gender);
    }

    public void completeRegistration(Long userId) {
        userRepository.updateFirstLoginStatus(userId);
    }



}
