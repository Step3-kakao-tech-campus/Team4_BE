package com.ktc.matgpt.review;

import com.ktc.matgpt.aws.FileValidator;
import com.ktc.matgpt.exception.CustomException;
import com.ktc.matgpt.exception.ErrorCode;
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
import com.ktc.matgpt.utils.TimeUnit;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
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
    private final EntityManager entityManager;

    private final int DEFAULT_PAGE_SIZE = 5;
    final static Long MIN = 60L;
    final static Long HOUR = MIN*60;
    final static Long DAY = HOUR*24;
    final static Long WEEK = DAY*7;
    final static Long MONTH = WEEK*4;
    final static Long YEAR = MONTH*12;


    public void completeReviewUpload(Long storeId, Long reviewId, ReviewRequest.CreateCompleteDTO requestDTO) {
        // 이미지 업로드 완료 후 리뷰, 이미지, 태그 정보 저장 로직
        // 이 로직은 이미지 업로드가 완료된 후 호출됩니다.

        //Store 프록시객체
        Store storeRef = storeService.getReferenceById(storeId);
        Review review = findReviewByIdOrThrow(reviewId);

        for (ReviewRequest.CreateCompleteDTO.ImageDTO imageDTO : requestDTO.getReviewImages()) {
            Image image = imageService.saveImageForReview(review, imageDTO.getImageUrl()); // 이미지 생성 및 리뷰에 매핑하여 저장
            saveTagsForImage(image, imageDTO, storeRef); // 태그 저장
        }
    }

    private void saveTagsForImage(Image image, ReviewRequest.CreateCompleteDTO.ImageDTO imageDTO, Store store) {
        for (ReviewRequest.CreateCompleteDTO.ImageDTO.TagDTO tagDTO : imageDTO.getTags()) {
            Food food = foodService.saveOrUpdateFoodByTag(tagDTO, store.getId()); //TODO: 태그에 들어갈 음식 검색하고 추가하는 로직
            tagService.saveTag(image, food, tagDTO);
        }
    }

    // 리뷰 생성 메서드
    public String createTemporaryReview(Long userId, Long storeId, ReviewRequest.SimpleCreateDTO simpleDTO) {
        //Store 프록시객체
        Store storeRef = storeService.getReferenceById(storeId);
        // 리뷰 데이터 저장
        Review review = Review.create(userId, storeRef, simpleDTO.getContent(), simpleDTO.getRating(), simpleDTO.getPeopleCount(), simpleDTO.getTotalPrice());
        reviewJPARepository.save(review);

        return review.getReviewUuid();
    }

    // Presigned URL 생성 메서드
    public List<ReviewResponse.UploadS3DTO.PresignedUrlDTO> createPresignedUrls(String reviewUuid, int imageCount) throws FileValidator.FileValidationException {
        List<ReviewResponse.UploadS3DTO.PresignedUrlDTO> presignedUrls = new ArrayList<>();

        for (int i = 1; i <= imageCount; i++) {
            String objectKey = generateObjectKey(reviewUuid, i); // 숫자를 사용하여 객체 키 생성
            URL presignedUrl = s3Service.getPresignedUrl(objectKey);

            presignedUrls.add(new ReviewResponse.UploadS3DTO.PresignedUrlDTO(objectKey, presignedUrl));
        }

        return presignedUrls;
    }

    public Review findReviewByIdOrThrow(Long reviewId) {
        return reviewJPARepository.findById(reviewId)
                .orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND));
    }

    private String generateObjectKey(String reviewUuid, int imageIndex) {
        // 리뷰 UUID와 순서를 조합하여 객체 키 생성
        return String.format("reviews/%s/%d", reviewUuid, imageIndex);
    }

    //태그에 음식, 이미지 매핑해서 저장


    @Transactional
    public void update(Long reviewId, Long userId, ReviewRequest.UpdateDTO requestDTO) {
        Review review = findReviewByIdOrThrow(reviewId);
        if (review.getUserId() != userId) {
            throw new IllegalArgumentException("review-" + review + ": 본인이 작성한 리뷰가 아닙니다. 수정할 수 없습니다.");
        }
        review.updateContent(requestDTO.getContent());
    }


    public ReviewResponse.FindByReviewIdDTO findDetailByReviewId(Long reviewId) {

        Review review = findReviewByIdOrThrow(reviewId);

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


        List<Review> reviews = switch (sortBy) {
            case "latest" -> reviewJPARepository.findAllByStoreIdAndOrderByIdDesc(storeId, cursorId, DEFAULT_PAGE_SIZE);
            case "rating" -> reviewJPARepository.findAllByStoreIdAndOrderByRatingDesc(storeId, cursorId, cursorRating, DEFAULT_PAGE_SIZE);
            default -> throw new IllegalArgumentException("Invalid sorting: " + sortBy);
        };

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
            case "best" -> PageRequest.of(0, limit, Sort.by(Sort.Order.desc("rating")));
            case "worst" -> PageRequest.of(0, limit, Sort.by(Sort.Order.asc("rating")));
            default -> throw new IllegalArgumentException("Invalid summaryType: " + summaryType);
        };

        return reviewJPARepository.findByStoreId(storeId, pageable);
    }

    public ReviewResponse.FindPageByUserIdDTO findAllByUserId(Long userId, String sortBy, int pageNum) {
        Page<Review> reviews = switch (sortBy) {
            case "latest" -> reviewJPARepository.findAllByUserIdAndOrderByIdDesc(userId, PageRequest.of(pageNum-1, DEFAULT_PAGE_SIZE));
            case "rating" -> reviewJPARepository.findAllByUserIdAndOrderByRatingDesc(userId, PageRequest.of(pageNum-1, DEFAULT_PAGE_SIZE));
            default -> throw new IllegalArgumentException("Invalid sorting: " + sortBy);
        };


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
        Review review = findReviewByIdOrThrow(reviewId);
        if (review.getUserId() != userId) {
            throw new IllegalArgumentException("review-" + review + ": 본인이 작성한 리뷰가 아닙니다. 삭제할 수 없습니다.");
        }
        imageService.deleteImagesByReviewId(reviewId);
        reviewJPARepository.deleteById(reviewId);
        log.info("review-%d: 리뷰가 삭제되었습니다.", review.getId());
    }



    private ReviewResponse.FindByReviewIdDTO.ReviewerDTO getReviewerDTO(Review review) {
        User user = userService.findById(review.getUserId());

        return ReviewResponse.FindByReviewIdDTO.ReviewerDTO.builder()
                        .userName(user.getName())
                        .profileImage("default profileImage")
                        .email(user.getEmail())
                        .build();
    }

    //TODO: Locale 이슈
    private String getRelativeTime(LocalDateTime time) {
        Duration duration = Duration.between(time, LocalDateTime.now());
        long seconds = duration.getSeconds();
        TimeUnit unit = TimeUnit.getAppropriateUnit(seconds);
        long value = (long) Math.floor((double) seconds / unit.getSeconds());
        String unitKey = unit.getKey();

        // 1이 아닌 경우에만 's'를 추가
        if (value != 1) {
            unitKey += "s";
        }
        return value + " " + unitKey + "ago";
        //        String timeUnitMessage = messageSourceAccessor.getMessage(unitKey, locale);
        //        return value + " " + timeUnitMessage + " " + messageSourceAccessor.getMessage("ago", locale);
    }

    public Review getReferenceById(Long reviewId) {
        try {
            return entityManager.getReference(Review.class, reviewId);
        } catch (EntityNotFoundException e) {
            throw new CustomException(ErrorCode.REVIEW_NOT_FOUND);
        }
    }

}