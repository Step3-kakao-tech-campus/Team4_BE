package com.ktc.matgpt.feature_review.review;

import com.ktc.matgpt.exception.Exception400;
import com.ktc.matgpt.feature_review.food.Food;
import com.ktc.matgpt.feature_review.food.FoodService;
import com.ktc.matgpt.feature_review.image.ImageService;
import com.ktc.matgpt.feature_review.review.dto.ReviewRequest;
import com.ktc.matgpt.feature_review.image.Image;
import com.ktc.matgpt.feature_review.review.dto.ReviewResponse;
import com.ktc.matgpt.feature_review.review.entity.Review;
import com.ktc.matgpt.feature_review.s3.S3Service;
import com.ktc.matgpt.feature_review.tag.Tag;
import com.ktc.matgpt.exception.Exception500;
import com.ktc.matgpt.feature_review.image.ImageJPARepository;
import com.ktc.matgpt.feature_review.tag.TagJPARepository;
import com.ktc.matgpt.feature_review.store.MockStore;
import com.ktc.matgpt.feature_review.tag.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    final static Long MIN = 60L;
    final static Long HOUR = MIN*60;
    final static Long DAY = HOUR*24;
    final static Long WEEK = DAY*7;
    final static Long MONTH = WEEK*4;
    final static Long YEAR = MONTH*12;

    @Transactional
    public Long create(MockStore mockStore, ReviewRequest.CreateDTO requestDTO/*, MultipartFile file*/) {
        int visitCount = requestDTO.getPeopleCount();
        Review review = Review.builder()
                .mockStore(mockStore)
                .content(requestDTO.getContent())
                .rating(requestDTO.getRating())
                .peopleCount(requestDTO.getPeopleCount())
                .totalPrice(requestDTO.getTotalPrice())
                .costPerPerson(requestDTO.getTotalPrice() / visitCount)
                .build();
        try {
            reviewJPARepository.save(review);
        } catch (Exception e) {
            throw new Exception500("리뷰 저장 실패");
        }
        
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
    public void update(Long reviewId, ReviewRequest.UpdateDTO requestDTO) {
        Review review = reviewJPARepository.findByReviewId(reviewId).orElseThrow(
                () -> new Exception400("존재하지 않는 리뷰입니다.")
        );
        review.updateContent(requestDTO.getContent());
    }


    public ReviewResponse.FindByReviewIdDTO findByReviewId(Long reviewId) {

        Review review = reviewJPARepository.findByReviewId(reviewId).orElseThrow(
                () -> new Exception400("존재하지 않는 리뷰입니다.")
        );

        List<Image> images = imageJPARepository.findAllByReviewId(reviewId);
        if (images.isEmpty()) throw new Exception400("존재하지 않는 이미지입니다.");
        List<ReviewResponse.FindByReviewIdDTO.ImageDTO> imageDTOs = new ArrayList<>();

        for (Image image : images) {
            List<Tag> tags = tagJPARepository.findAllByImageId(image.getId());
            if (tags.isEmpty()) throw new Exception400("존재하지 않는 태그입니다.");
            imageDTOs.add(new ReviewResponse.FindByReviewIdDTO.ImageDTO(image, tags));
        }

        List<Long> relativeTime = getRelativeTime(review.getCreatedAt());

        return new ReviewResponse.FindByReviewIdDTO(review, relativeTime, imageDTOs);
    }


    public List<ReviewResponse.FindAllByStoreIdDTO> findAllByStoreId(Long storeId) {

        List<Review> reviews = reviewJPARepository.findAllByStoreId(storeId);
        if (reviews.isEmpty()) throw new Exception400("음식점에 등록된 리뷰가 없습니다.");

        List<ReviewResponse.FindAllByStoreIdDTO> responseDTOs = new ArrayList<>();

        for (Review review : reviews) {
            List<String> imageUrls = imageJPARepository.findAllImagesByReviewId(review.getId());
            List<Long> relativeTime = getRelativeTime(review.getCreatedAt());
            responseDTOs.add(new ReviewResponse.FindAllByStoreIdDTO(review, relativeTime, imageUrls));
        }
        return responseDTOs;
    }



//    relativeTime : [sec, min, hour, day, week, month, year] ago
    private List<Long> getRelativeTime(LocalDateTime time) {
        Duration duration = Duration.between(time, LocalDateTime.now());
        List<Long> relativeTime = new ArrayList<>();
        Long seconds = duration.getSeconds();

        if (seconds<MIN) relativeTime.add(0, seconds);
        else if (seconds<HOUR) relativeTime.add(1, seconds/MIN);
        else if (seconds<DAY) relativeTime.add(2, seconds/HOUR);
        else if (seconds<WEEK) relativeTime.add(3, seconds/DAY);
        else if (seconds<MONTH) relativeTime.add(4, seconds/WEEK);
        else if (seconds<YEAR) relativeTime.add(5, seconds/MONTH);
        else relativeTime.add(6, seconds/YEAR);

        return relativeTime;
    }


}