package com.ktc.matgpt.review;

import com.ktc.matgpt.food.Food;
import com.ktc.matgpt.food.FoodService;
import com.ktc.matgpt.image.Image;
import com.ktc.matgpt.image.ImageService;
import com.ktc.matgpt.review.dto.ReviewRequest;
import com.ktc.matgpt.review.dto.ReviewResponse;
import com.ktc.matgpt.review.entity.Review;
import com.ktc.matgpt.aws.S3Service;
import com.ktc.matgpt.tag.Tag;
import com.ktc.matgpt.tag.TagService;
import com.ktc.matgpt.store.Store;
import com.ktc.matgpt.store.StoreService;
import com.ktc.matgpt.user.entity.User;
import com.ktc.matgpt.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReviewService {
    private final ReviewJPARepository reviewJPARepository;
    private final S3Service s3Service;
    private final ImageService imageService;
    private final FoodService foodService;
    private final TagService tagService;
    private final UserService userService;
    private final StoreService storeService;
    private final MessageSourceAccessor messageSourceAccessor;

    private final int DEFAULT_PAGE_SIZE = 5;
    final static Long MIN = 60L;
    final static Long HOUR = MIN*60;
    final static Long DAY = HOUR*24;
    final static Long WEEK = DAY*7;
    final static Long MONTH = WEEK*4;
    final static Long YEAR = MONTH*12;


    public ReviewResponse.UploadS3DTO createReview(Long userId, Long storeId, ReviewRequest.CreateDTO requestDTO) {

        //Store 프록시객체
        Store storeRef = storeService.getReferenceById(storeId);

        // 리뷰 데이터 저장
        Review review = Review.create(userId, storeRef, requestDTO.getContent(), requestDTO.getRating(), requestDTO.getPeopleCount(), requestDTO.getTotalPrice());
        reviewJPARepository.save(review);

        // presigned URL 생성
        List<ReviewResponse.UploadS3DTO.PresignedUrlDTO> presignedUrls = new ArrayList<>();
        for (ReviewRequest.CreateDTO.ImageDTO imageDTO : requestDTO.getReviewImages()) {
            String objectKey = generateObjectKey(review, imageDTO.getImage());
            URL presignedUrl = s3Service.getPresignedUrl(objectKey);

            presignedUrls.add(new ReviewResponse.UploadS3DTO.PresignedUrlDTO(objectKey, presignedUrl));
        }

        return new ReviewResponse.UploadS3DTO(review.getId(), presignedUrls);
    }

    private String generateObjectKey(Review review, MultipartFile image) {
        return String.format("reviews/%d/%s", review.getId(), image.getOriginalFilename());
    }

    private void saveTagsForImage(Image image, ReviewRequest.CreateDTO.ImageDTO imageDTO, Store store) {
        for (ReviewRequest.CreateDTO.ImageDTO.TagDTO tagDTO : imageDTO.getTags()) {
            Food food = foodService.saveOrUpdateFoodByTag(tagDTO, store.getId());
            tagService.saveTag(image, food, tagDTO);
        }
    }


    @Transactional
    public void update(Long reviewId, Long userId, ReviewRequest.UpdateDTO requestDTO) {
        Review review = reviewJPARepository.findByReviewId(reviewId).orElseThrow(
                () -> new NoSuchElementException("reviewId-" + reviewId + ": 존재하지 않는 리뷰입니다. 수정할 수 없습니다.")
        );

        if (review.getUserId() != userId) {
            throw new IllegalArgumentException("review-" + review + ": 본인이 작성한 리뷰가 아닙니다. 수정할 수 없습니다.");
        }

        review.updateContent(requestDTO.getContent());
    }


    public ReviewResponse.FindByReviewIdDTO findDetailByReviewId(Long reviewId) {

        Review review = reviewJPARepository.findByReviewId(reviewId).orElseThrow(
                () -> new NoSuchElementException("reviewId-" + reviewId + ": 리뷰를 찾을 수 없습니다.")
        );

        ReviewResponse.FindByReviewIdDTO.ReviewerDTO reviewerDTO = getReviewerDTO(review);

        List<Image> images = imageService.getImagesByReviewId(reviewId);
        if (images.isEmpty()) log.info("review-" + review.getId() + "리뷰에 등록된 이미지가 없습니다.");


        List<ReviewResponse.FindByReviewIdDTO.ImageDTO> imageDTOs = new ArrayList<>();

        for (Image image : images) {
            List<Tag> tags = tagService.getTagsByImageId(image.getId());
            if (tags.isEmpty()) log.info("image-" + image.getId() + ": 이미지에 등록된 태그가 없습니다.");

            imageDTOs.add(new ReviewResponse.FindByReviewIdDTO.ImageDTO(image, tags));
        }
        String relativeTime = getRelativeTime(review.getCreatedAt());

        return new ReviewResponse.FindByReviewIdDTO(review, reviewerDTO, imageDTOs, relativeTime);
    }


    public List<ReviewResponse.FindAllByStoreIdDTO> findAllByStoreId(Long storeId, String sortBy, Long cursorId, double cursorRating) {

        List<Review> reviews = (sortBy.equals("latest"))
                ? reviewJPARepository.findAllByStoreIdAndOrderByIdDesc(storeId, cursorId, DEFAULT_PAGE_SIZE)
                : reviewJPARepository.findAllByStoreIdAndOrderByRatingDesc(storeId, cursorId, cursorRating, DEFAULT_PAGE_SIZE);

        if (reviews.isEmpty()) {
            throw new NoSuchElementException("storeId-" + storeId + ": 음식점에 등록된 리뷰가 없습니다.");
        }

        List<ReviewResponse.FindAllByStoreIdDTO> responseDTOs = new ArrayList<>();
        for (Review review : reviews) {
            List<String> imageUrls = imageService.getImageUrlsByReviewId(review.getId());

            if (imageUrls.isEmpty()) {
                log.info("review-" + review.getId() + ": 리뷰에 등록된 이미지가 없습니다.");
            }

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

    public ReviewResponse.FindPageByUserIdDTO findAllByUserId(Long userId, String sortBy, int pageNum) {
        Page<Review> reviews = (sortBy.equals("latest"))
                ? reviewJPARepository.findAllByUserIdAndOrderByIdDesc(userId, PageRequest.of(pageNum-1, DEFAULT_PAGE_SIZE))
                : reviewJPARepository.findAllByUserIdAndOrderByRatingDesc(userId, PageRequest.of(pageNum-1, DEFAULT_PAGE_SIZE));

        if (reviews.isEmpty()) {
            throw new NoSuchElementException("user-" + userId + ": 회원이 작성한 리뷰가 없습니다.");
        }

        List<ReviewResponse.FindPageByUserIdDTO.FindByUserIdDTO> reviewDTOs = new ArrayList<>();

        for (Review review : reviews) {
            List<String> imageUrls = imageService.getImageUrlsByReviewId(review.getId());
            if (imageUrls.isEmpty()) {
                log.info("reviewId-" + review.getId() + ": 리뷰에 등록된 이미지가 없습니다.");
            }

            String relativeTime = getRelativeTime(review.getCreatedAt());
            reviewDTOs.add(new ReviewResponse.FindPageByUserIdDTO.FindByUserIdDTO(review, relativeTime, imageUrls));
        }
        return new ReviewResponse.FindPageByUserIdDTO(reviewDTOs, reviews);
    }


    @Transactional
    public void delete(Long reviewId, Long userId) {
        Review review = reviewJPARepository.findByReviewId(reviewId).orElseThrow(
                () -> new NoSuchElementException("review-" + reviewId + ": 존재하지 않는 리뷰입니다. 삭제할 수 없습니다.")
        );
        if (review.getUserId() != userId) {
            throw new IllegalArgumentException("review-" + review + ": 본인이 작성한 리뷰가 아닙니다. 삭제할 수 없습니다.");
        }
        imageService.deleteImagesByReviewId(reviewId);
        reviewJPARepository.deleteById(reviewId);
    }



    private ReviewResponse.FindByReviewIdDTO.ReviewerDTO getReviewerDTO(Review review) {
        User user = userService.findById(review.getUserId());

        return ReviewResponse.FindByReviewIdDTO.ReviewerDTO.builder()
                        .userName(user.getName())
                        .profileImage("default profileImage")
                        .email(user.getEmail())
                        .build();
    }


    private String getRelativeTime(LocalDateTime time) {
        Duration duration = Duration.between(time, LocalDateTime.now());
        Long seconds = duration.getSeconds();

        // TODO: user.getCountry()로 받아오도록 구현
        Locale country = Locale.KOREA;  // 임시로 한국 설정

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