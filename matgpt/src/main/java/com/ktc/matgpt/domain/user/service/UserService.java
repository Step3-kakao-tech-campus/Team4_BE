package com.ktc.matgpt.domain.user.service;

import com.ktc.matgpt.domain.aws.S3Service;
import com.ktc.matgpt.domain.image.Image;
import com.ktc.matgpt.domain.review.dto.ReviewRequest;
import com.ktc.matgpt.domain.review.entity.Review;
import com.ktc.matgpt.domain.store.Store;
import com.ktc.matgpt.domain.user.dto.UserDto;
import com.ktc.matgpt.domain.user.entity.AgeGroup;
import com.ktc.matgpt.domain.user.entity.Gender;
import com.ktc.matgpt.domain.user.entity.LocaleEnum;
import com.ktc.matgpt.domain.user.entity.User;
import com.ktc.matgpt.domain.user.repository.UserRepository;
import com.ktc.matgpt.exception.ErrorMessage;
import com.ktc.matgpt.exception.auth.UnAuthorizedException;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URL;
import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final EntityManager entityManager;
    private final S3Service s3Service;

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
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

    public void updateUserAdditionalInfo(String email, UserDto.Request userDto) {
        User user = findByEmail(email);
        user.setName(userDto.getNickname());
        user.setGender(Gender.fromString(userDto.getGender()));
        user.setAgeGroup(AgeGroup.fromInt(userDto.getAge()));

        LocaleEnum localeEnum = LocaleEnum.fromString(userDto.getLocale());
        if (localeEnum != null) {
            user.setLocale(localeEnum);
        } else {
            throw new NoSuchElementException(ErrorMessage.LOCALE_NOT_FOUND);
        }

        userRepository.save(user);
    }
    @Transactional
    public String generatePresignedUrl(String email) {
        URL presignedUrl = s3Service.getPresignedUrl(String.format("users/%s", email));
        return presignedUrl.toString();
    }

    @Transactional
    public void completeImageUpload(UserDto.ImageRequest imageRequest, String userEmail) {
        User user = findByEmail(userEmail);
        user.setProfileImageUrl(imageRequest.getPresignedUrl());

        userRepository.save(user);
    }

}
