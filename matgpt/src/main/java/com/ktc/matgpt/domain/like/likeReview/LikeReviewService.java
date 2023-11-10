package com.ktc.matgpt.domain.like.likeReview;

import com.ktc.matgpt.domain.review.entity.Review;
import com.ktc.matgpt.domain.user.entity.User;
import com.ktc.matgpt.utils.paging.CursorRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class LikeReviewService {
    private final LikeReviewJPARepository likeReviewJPARepository;
    private static final int DEFAULT_PAGE_SIZE = 8;

    @Transactional
    public boolean toggleLikeForReview(User userRef, Review review) {

        if (isLikeAlreadyExists(userRef, review)) {
            deleteLikeToReview(userRef, review);
            log.info("review-{}: 리뷰에 좋아요가 해제되었습니다.", review.getId());
            return false;
        } else {
            addLikeToReview(userRef, review);
            log.info("review-{}: 리뷰에 좋아요가 등록되었습니다.", review.getId());
            return true;
        }
    }

    @Transactional(readOnly = true)
    public List<LikeReview> findLikeReviewsByUserId(Long userId, CursorRequest page) {
        return likeReviewJPARepository.findAllByUserIdAndOrderByIdDesc(userId, page.cursorId, page.request);
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

    public boolean isLikeAlreadyExists(User userRef, Review review) {
        return likeReviewJPARepository.existsByUserAndReview(userRef, review);
    }
}
