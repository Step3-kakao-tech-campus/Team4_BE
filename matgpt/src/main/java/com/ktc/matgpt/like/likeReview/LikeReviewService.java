package com.ktc.matgpt.like.likeReview;

import com.ktc.matgpt.review.ReviewService;
import com.ktc.matgpt.review.entity.Review;
import com.ktc.matgpt.user.entity.User;
import com.ktc.matgpt.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class LikeReviewService {
    private final UserService userService;
    private final LikeReviewJPARepository likeReviewJPARepository;
    private static final int DEFAULT_PAGE_SIZE = 5;

    @Transactional
    public boolean toggleLikeForReview(User userRef, Review review) {

        if (isLikeAlreadyExists(userRef, review)) {
            deleteLikeToReview(userRef, review);
            return false;
        } else {
            addLikeToReview(userRef, review);
            return true;
        }
    }

    public LikeReviewResponseDTO.FindLikeReviewsDTO findReviewsByUserId(Long userId, int pageNum) {
        User user = userService.findById(userId);
        PageRequest page = PageRequest.of(pageNum-1, DEFAULT_PAGE_SIZE);
        List<LikeReview> likeReviewList = likeReviewJPARepository.findAllByUserIdAndOrderByIdDesc(user.getId(), page).stream().toList();

        return new LikeReviewResponseDTO.FindLikeReviewsDTO(likeReviewList);
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
