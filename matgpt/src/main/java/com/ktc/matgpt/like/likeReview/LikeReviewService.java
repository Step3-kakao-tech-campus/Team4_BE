package com.ktc.matgpt.like.likeReview;

import com.ktc.matgpt.exception.CustomException;
import com.ktc.matgpt.exception.ErrorCode;
import com.ktc.matgpt.review.entity.Review;
import com.ktc.matgpt.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
            log.info("review-%d: 리뷰에 좋아요가 해제되었습니다.", review.getId());
            return false;
        } else {
            addLikeToReview(userRef, review);
            log.info("review-%d: 리뷰에 좋아요가 등록되었습니다.", review.getId());
            return true;
        }
    }

    public Page<LikeReview> findReviewsByUserId(Long userId, Long cursorId) {
        PageRequest page = PageRequest.ofSize(DEFAULT_PAGE_SIZE);
        Page<LikeReview> likeReviewList = likeReviewJPARepository.findAllByUserIdAndOrderByIdDesc(userId, cursorId, page);

        if (likeReviewList.isEmpty()) {
            throw new CustomException(ErrorCode.REVIEW_LIST_NOT_FOUND);
        }

        return likeReviewList;
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
