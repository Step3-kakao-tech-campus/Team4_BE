package com.ktc.matgpt.like.likeReview;

import com.ktc.matgpt.like.likeStore.LikeStore;
import com.ktc.matgpt.like.likeStore.LikeStoreResponseDTO;
import com.ktc.matgpt.review.ReviewService;
import com.ktc.matgpt.review.entity.Review;
import com.ktc.matgpt.store.Store;
import com.ktc.matgpt.store.StoreService;
import com.ktc.matgpt.user.entity.User;
import com.ktc.matgpt.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class LikeReviewService {
    private final UserService userService;
    private final ReviewService reviewService;
    private final StoreService storeService;
    private final LikeReviewJPARepository likeReviewJPARepository;

    @Transactional
    public boolean toggleLikeForReview(Long reviewId, String email) {

        User userRef = userService.getReferenceByEmail(email);
        Review reviewRef = reviewService.getReferenceById(reviewId);
        Store storeRef = storeService.getReferenceById(reviewRef.getStore().getId());

        if (isLikeAlreadyExists(userRef, reviewRef)) {
            deleteLikeToReview(userRef, reviewRef);
            return false;
        } else {
            addLikeToReview(userRef, storeRef, reviewRef);
            return true;
        }
    }

    public LikeReviewResponseDTO.FindAllLikeReviewsDTO findReviewsByUserEmail(String email) {
        User user = userService.findByEmail(email);
        List<Review> reviewList = likeReviewJPARepository.findLikedReviewsByUserId(user.getId()).stream().toList();
        return new LikeReviewResponseDTO.FindAllLikeReviewsDTO(user,reviewList);
    }

    @Transactional
    public void addLikeToReview(User userRef, Store storeRef, Review reviewRef){
        likeReviewJPARepository.save(LikeReview.create(userRef, storeRef, reviewRef));
    }

    @Transactional
    public void deleteLikeToReview(User userRef, Review reviewRef) {
        likeReviewJPARepository.deleteByUserAndReview(userRef, reviewRef);
    }

    private boolean isLikeAlreadyExists(User userRef, Review reviewRef) {
        return likeReviewJPARepository.existsByUserAndReview(userRef, reviewRef);
    }
}
