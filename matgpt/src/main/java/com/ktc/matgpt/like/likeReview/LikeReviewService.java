package com.ktc.matgpt.like.likeReview;

import com.ktc.matgpt.review.ReviewService;
import com.ktc.matgpt.review.entity.Review;
import com.ktc.matgpt.user.entity.User;
import com.ktc.matgpt.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class LikeReviewService {
    private final UserService userService;
    private final ReviewService reviewService;
    private final LikeReviewJPARepository likeReviewJPARepository;

    @Transactional
    public boolean toggleLikeForReview(Long reviewId, String email) {

        User userRef = userService.getReferenceByEmail(email);
        Review review = reviewService.findReviewByIdOrThrow(reviewId);

        if (isLikeAlreadyExists(userRef, review)) {
            deleteLikeToReview(userRef, review);
            review.minusRecommendCount();
            return false;
        } else {
            addLikeToReview(userRef, review);
            review.plusRecommendCount();
            return true;
        }
    }

    public List<LikeReviewResponseDTO.FindLikeReviewDTO> findReviewsByUserEmail(String email) {
        User user = userService.findByEmail(email);
        List<LikeReview> likeReviewList = likeReviewJPARepository.findAllByUserId(user.getId()).stream().toList();

        return likeReviewList.stream().map(likeReview -> {
            Review review = likeReview.getReview();
            User reviewer = likeReview.getUser();
            return new LikeReviewResponseDTO.FindLikeReviewDTO(review, reviewer.getId(), reviewer.getName(), ""/*profileImage()*/);
        }).collect(Collectors.toList());
    }

    @Transactional
    public void deleteAllByReviewId(Long reviewId) {
        likeReviewJPARepository.deleteAllByReviewId(reviewId);
    }

    @Transactional
    public void addLikeToReview(User userRef, Review review){
        likeReviewJPARepository.save(LikeReview.create(userRef, review));
    }

    @Transactional
    public void deleteLikeToReview(User userRef, Review review) {
        likeReviewJPARepository.deleteByUserAndReview(userRef, review);
    }

    private boolean isLikeAlreadyExists(User userRef, Review review) {
        return likeReviewJPARepository.existsByUserAndReview(userRef, review);
    }
}
