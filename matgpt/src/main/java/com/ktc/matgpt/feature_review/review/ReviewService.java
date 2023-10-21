package com.ktc.matgpt.feature_review.review;

import com.ktc.matgpt.exception.Exception400;
import com.ktc.matgpt.feature_review.food.Food;
import com.ktc.matgpt.feature_review.food.FoodService;
import com.ktc.matgpt.feature_review.image.Image;
import com.ktc.matgpt.feature_review.image.ImageJPARepository;
import com.ktc.matgpt.feature_review.image.ImageService;
import com.ktc.matgpt.feature_review.review.dto.ReviewRequest;
import com.ktc.matgpt.feature_review.review.dto.ReviewResponse;
import com.ktc.matgpt.feature_review.review.entity.Review;
import com.ktc.matgpt.feature_review.s3.S3Service;
import com.ktc.matgpt.feature_review.tag.Tag;
import com.ktc.matgpt.feature_review.tag.TagJPARepository;
import com.ktc.matgpt.feature_review.tag.TagService;
import com.ktc.matgpt.store.Store;
import com.ktc.matgpt.user.entity.User;
import com.ktc.matgpt.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

@RequiredArgsConstructor
@Service
public class ReviewService {
    private final ReviewJPARepository reviewJPARepository;
    private final S3Service s3Service;
    private final ImageService imageService;
    private final ImageJPARepository imageJPARepository;
    private final FoodService foodService;
    private final TagService tagService;
    private final TagJPARepository tagJPARepository;
    private final UserRepository userRepository;
    private final MessageSourceAccessor messageSourceAccessor;

    private final int DEFAULT_PAGE_SIZE = 5;
    final static Long MIN = 60L;
    final static Long HOUR = MIN*60;
    final static Long DAY = HOUR*24;
    final static Long WEEK = DAY*7;
    final static Long MONTH = WEEK*4;
    final static Long YEAR = MONTH*12;

    @Transactional
    public Long create(Long userId, Store store, ReviewRequest.CreateDTO requestDTO/*, MultipartFile file*/) {
        int visitCount = requestDTO.getPeopleCount();
        Review review = Review.builder()
                .userId(userId)
                .store(store)
                .content(requestDTO.getContent())
                .rating(requestDTO.getRating())
                .peopleCount(requestDTO.getPeopleCount())
                .totalPrice(requestDTO.getTotalPrice())
                .costPerPerson(requestDTO.getTotalPrice() / visitCount)
                .build();
        reviewJPARepository.save(review);

        for (ReviewRequest.CreateDTO.ImageDTO imageDTO : requestDTO.getReviewImages()) {
//            String imageUrl = s3Service.save(imageDTO.getImage());
            Image image = imageService.save(imageDTO, review/*, imageUrl*/);

            for (ReviewRequest.CreateDTO.ImageDTO.TagDTO tagDTO : imageDTO.getTags()) {
                Food food = foodService.save(tagDTO);
                tagService.save(image, food, tagDTO);
            }
        }
        return review.getId();
    }


    @Transactional
    public void update(Long reviewId, Long userId, ReviewRequest.UpdateDTO requestDTO) {
        Review review = reviewJPARepository.findByReviewId(reviewId).orElseThrow(
                () -> new Exception400("reviewId-" + reviewId + ": 존재하지 않는 리뷰입니다. 수정할 수 없습니다.")
        );

        if (review.getUserId() != userId) {
            throw new Exception400("review-" + review + ": 본인이 작성한 리뷰가 아닙니다. 수정할 수 없습니다.");
        }

        review.updateContent(requestDTO.getContent());
    }


    public ReviewResponse.FindByReviewIdDTO findByReviewId(Long reviewId) {

        Review review = reviewJPARepository.findByReviewId(reviewId).orElseThrow(
                () -> new Exception400("reviewId-" + reviewId + ": 리뷰를 찾을 수 없습니다.")
        );

        ReviewResponse.FindByReviewIdDTO.ReviewerDTO reviewerDTO = getReviewerDTO(review);

        List<Image> images = imageJPARepository.findAllByReviewId(reviewId);
        if (images.isEmpty()) throw new Exception400("reviewId-" + reviewId + ": 리뷰에 등록된 이미지가 없습니다.");

        List<ReviewResponse.FindByReviewIdDTO.ImageDTO> imageDTOs = new ArrayList<>();

        for (Image image : images) {
            List<Tag> tags = tagJPARepository.findAllByImageId(image.getId());
            if (tags.isEmpty()) throw new Exception400("imageId-" + image.getId() + ": 이미지에 등록된 태그가 없습니다.");
            imageDTOs.add(new ReviewResponse.FindByReviewIdDTO.ImageDTO(image, tags));
        }
        String relativeTime = getRelativeTime(review.getCreatedAt());

        return new ReviewResponse.FindByReviewIdDTO(review, reviewerDTO, imageDTOs, relativeTime);
    }


    public List<ReviewResponse.FindAllByStoreIdDTO> findAllByStoreId(Long storeId, String sortBy, Long cursorId, double cursorRating) {

        List<Review> reviews = (sortBy.equals("latest"))
                ? reviewJPARepository.findAllByStoreIdAndOrderByIdDesc(storeId, cursorId, DEFAULT_PAGE_SIZE)
                : reviewJPARepository.findAllByStoreIdAndOrderByRatingDesc(storeId, cursorId, cursorRating, DEFAULT_PAGE_SIZE);

        if (reviews.isEmpty()) throw new Exception400("storeId-" + storeId + ": 음식점에 등록된 리뷰가 없습니다.");

        List<ReviewResponse.FindAllByStoreIdDTO> responseDTOs = new ArrayList<>();
        for (Review review : reviews) {
            List<String> imageUrls = imageJPARepository.findAllImagesByReviewId(review.getId());
            if (imageUrls.isEmpty()) throw new Exception400("reviewId-" + review.getId() + ": 리뷰에 등록된 이미지가 없습니다.");

            String relativeTime = getRelativeTime(review.getCreatedAt());
            responseDTOs.add(new ReviewResponse.FindAllByStoreIdDTO(review, relativeTime, imageUrls));
        }
        return responseDTOs;
    }

    public List<Review> findByStoreIdAndSummaryType(Long storeId, String summaryType, int limit) {
        Pageable pageable = switch (summaryType) {
            case "BEST" -> PageRequest.of(0, limit, Sort.by(Sort.Order.desc("rating")));
            case "WORST" -> PageRequest.of(0, limit, Sort.by(Sort.Order.asc("rating")));
            default -> throw new IllegalArgumentException("Invalid summaryType: " + summaryType);
        };

        return reviewJPARepository.findByStoreId(storeId, pageable);
    }

    public List<ReviewResponse.FindAllByUserIdDTO> findAllByUserId(Long userId, String sortBy, int pageNum) {
        List<Review> reviews = (sortBy.equals("latest"))
                ? reviewJPARepository.findAllByUserIdAndOrderByIdDesc(userId, pageNum, DEFAULT_PAGE_SIZE)
                : reviewJPARepository.findAllByUserIdAndOrderByRatingDesc(userId, pageNum, DEFAULT_PAGE_SIZE);

        if (reviews.isEmpty()) throw new Exception400("회원이 작성한 리뷰가 없습니다.");

        List<ReviewResponse.FindAllByUserIdDTO> responseDTOs = new ArrayList<>();

        for (Review review : reviews) {
            List<String> imageUrls = imageJPARepository.findAllImagesByReviewId(review.getId());
            if (imageUrls.isEmpty()) throw new Exception400("reviewId-" + review.getId() + ": 리뷰에 등록된 이미지가 없습니다.");

            String relativeTime = getRelativeTime(review.getCreatedAt());
            responseDTOs.add(new ReviewResponse.FindAllByUserIdDTO(review, relativeTime, imageUrls));
        }
        return responseDTOs;
    }


    @Transactional
    public void delete(Long reviewId, Long userId) {
        Review review = reviewJPARepository.findByReviewId(reviewId).orElseThrow(
                () -> new Exception400("reveiw-" + reviewId + ": 존재하지 않는 리뷰입니다. 삭제할 수 없습니다.")
        );
        if (review.getUserId() != userId) {
            throw new Exception400("review-" + review + ": 본인이 작성한 리뷰가 아닙니다. 삭제할 수 없습니다.");
        }
        imageService.deleteAll(reviewId);
        reviewJPARepository.deleteById(reviewId);
    }



    private ReviewResponse.FindByReviewIdDTO.ReviewerDTO getReviewerDTO(Review review) {
        Optional<User> user = userRepository.findById(review.getUserId());
        if (user.isEmpty()) throw new Exception400("리뷰 작성자 정보를 불러올 수 없습니다. 현재 등록되지 않은 사용자입니다.");

        ReviewResponse.FindByReviewIdDTO.ReviewerDTO reviewerDTO =
                ReviewResponse.FindByReviewIdDTO.ReviewerDTO.builder()
                        .userName(user.get().getName())
                        .profileImage("기본 프로필 이미지")   // User Entity에 profileImage 필드가 없음
                        .email(user.get().getEmail())
                        .build();
        return reviewerDTO;
    }


    private String getRelativeTime(LocalDateTime time) {
        Duration duration = Duration.between(time, LocalDateTime.now());
        Long seconds = duration.getSeconds();
        Locale country = Locale.KOREA;  // 임시로 한국 설정, user.getCountry()로 받아오도록 수정 필요

        String msg;
        if (seconds<MIN) msg = seconds + messageSourceAccessor.getMessage("sec", country);
        else if (seconds<HOUR) msg = seconds/MIN + messageSourceAccessor.getMessage("min", country);
        else if (seconds<DAY) msg = seconds/HOUR + messageSourceAccessor.getMessage("hour", country);
        else if (seconds<WEEK) msg = seconds/DAY + messageSourceAccessor.getMessage("day", country);
        else if (seconds<MONTH) msg = seconds/WEEK + messageSourceAccessor.getMessage("week", country);
        else if (seconds<YEAR) msg = seconds/MONTH + messageSourceAccessor.getMessage("month", country);
        else msg = seconds/YEAR + messageSourceAccessor.getMessage("year", country);

        return msg + " " + messageSourceAccessor.getMessage("ago", country);
    }
}