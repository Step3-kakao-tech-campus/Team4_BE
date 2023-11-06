package com.ktc.matgpt.review;

import com.ktc.matgpt.aws.FileValidator;
import com.ktc.matgpt.exception.CustomException;
import com.ktc.matgpt.exception.ErrorCode;
import com.ktc.matgpt.food.Food;
import com.ktc.matgpt.food.FoodService;
import com.ktc.matgpt.image.Image;
import com.ktc.matgpt.image.ImageService;
import com.ktc.matgpt.like.likeReview.LikeReviewService;
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
import com.ktc.matgpt.utils.PageResponse;
import com.ktc.matgpt.utils.Paging;
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
    private final LikeReviewService likeReviewService;
    private final MessageSourceAccessor messageSourceAccessor;
    private final EntityManager entityManager;

    private static final int DEFAULT_PAGE_SIZE = 8;
    private static final int DEFAULT_PAGE_SIZE_PLUS_ONE = DEFAULT_PAGE_SIZE + 1;
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

        // TODO: 리뷰 생성 시 Store의 리뷰 개수와 평점 업데이트 구현 - Store 프록시 객체를 사용하고 있어 보류

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
    public Review createTemporaryReview(Long userId, Long storeId, ReviewRequest.SimpleCreateDTO simpleDTO) {
        //Store 프록시객체
        Store storeRef = storeService.getReferenceById(storeId);
        // 리뷰 데이터 저장
        Review review = Review.create(userId, storeRef, simpleDTO.getContent(), simpleDTO.getRating(), simpleDTO.getPeopleCount(), simpleDTO.getTotalPrice());
        reviewJPARepository.save(review);

        return review;
    }

    // Presigned URL 생성 메서드
    public List<ReviewResponse.UploadS3DTO.PresignedUrlDTO> createPresignedUrls(String reviewUuid, int imageCount) {
        List<ReviewResponse.UploadS3DTO.PresignedUrlDTO> presignedUrls = new ArrayList<>();

        for (int i = 1; i <= imageCount; i++) {
            URL presignedUrl = s3Service.getPresignedUrl(generateObjectKey(reviewUuid, i));
            presignedUrls.add(new ReviewResponse.UploadS3DTO.PresignedUrlDTO(presignedUrl));
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
    public void updateContent(Long reviewId, Long userId, ReviewRequest.UpdateDTO requestDTO) {
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

    public ReviewResponse.FindPageByStoreIdDTO findAllByStoreId(Long storeId, String sortBy, Long cursorId, int cursorLikes) {
        Pageable page = PageRequest.ofSize(DEFAULT_PAGE_SIZE);
        Page<Review> reviews = switch (sortBy) {
            case "latest" -> reviewJPARepository.findAllByStoreIdAndOrderByIdDesc(storeId, cursorId, page);
            case "likes" -> reviewJPARepository.findAllByStoreIdAndOrderByLikesAndIdDesc(storeId, cursorId, cursorLikes, page);
            default -> throw new IllegalArgumentException("Invalid sorting: " + sortBy);
        };

        if (reviews.isEmpty()) {
            throw new CustomException(ErrorCode.REVIEW_LIST_NOT_FOUND);
        }

        List<ReviewResponse.FindPageByStoreIdDTO.StoreReviewDTO> reviewDTOs = new ArrayList<>();
        for (Review review : reviews) {
            List<String> imageUrls = imageService.getImageUrlsByReviewId(review.getId());

            if (imageUrls.isEmpty()) {
                log.info("review-" + review.getId() + ": 리뷰에 등록된 이미지가 없습니다.");
            }

            String relativeTime = getRelativeTime(review.getCreatedAt());
            reviewDTOs.add(new ReviewResponse.FindPageByStoreIdDTO.StoreReviewDTO(review, relativeTime, imageUrls));
        }
        return new ReviewResponse.FindPageByStoreIdDTO(reviews, reviewDTOs);
    }

    public List<Review> findByStoreIdAndSummaryType(Long storeId, String summaryType, int limit) {
        Pageable pageable = switch (summaryType) {
            case "best" -> PageRequest.of(0, limit, Sort.by(Sort.Order.desc("rating")));
            case "worst" -> PageRequest.of(0, limit, Sort.by(Sort.Order.asc("rating")));
            default -> throw new IllegalArgumentException("Invalid summaryType: " + summaryType);
        };

        return reviewJPARepository.findByStoreId(storeId, pageable);
    }

    public ReviewResponse.FindPageByUserIdDTO findAllByUserId(Long userId, String sortBy, Long cursorId, int cursorLikes) {
        Pageable page = PageRequest.ofSize(DEFAULT_PAGE_SIZE);
        Page<Review> reviews = switch (sortBy) {
            case "latest" -> reviewJPARepository.findAllByUserIdAndOrderByIdDesc(userId, cursorId, page);
            case "likes" -> reviewJPARepository.findAllByUserIdAndOrderByLikesAndIdDesc(userId, cursorId, cursorLikes, page);
            default -> throw new IllegalArgumentException("Invalid sorting: " + sortBy);
        };


        if (reviews.isEmpty()) {
            throw new CustomException(ErrorCode.REVIEW_LIST_NOT_FOUND);
        }

        List<ReviewResponse.FindPageByUserIdDTO.UserReviewDTO> reviewDTOs = new ArrayList<>();

        for (Review review : reviews) {
            String relativeTime = getRelativeTime(review.getCreatedAt());
            reviewDTOs.add(new ReviewResponse.FindPageByUserIdDTO.UserReviewDTO(review, relativeTime));
        }
        return new ReviewResponse.FindPageByUserIdDTO(reviews, reviewDTOs);
    }


    @Transactional
    public void delete(Long reviewId, Long userId) {
        Review review = findReviewByIdOrThrow(reviewId);
        if (review.getUserId() != userId) {
            throw new IllegalArgumentException("review-" + review + ": 본인이 작성한 리뷰가 아닙니다. 삭제할 수 없습니다.");
        }
        // 이미지(+태그) 삭제
        imageService.deleteImagesByReviewId(reviewId);

        // Store 리뷰 개수, 평점 필드 업데이트
        Store store = storeService.findById(review.getStore().getId());
        store.removeReview(review.getRating());

        // 리뷰 삭제
        likeReviewService.deleteAllByReviewId(reviewId);
        reviewJPARepository.deleteById(reviewId);
        log.info("review-%d: 리뷰가 삭제되었습니다.", review.getId());
    }

    public PageResponse<LocalDateTime, ReviewResponse.RecentReviewDTO> getRecentlyReviewedStores(Long cursorId, LocalDateTime cursor) {
        Pageable page = Pageable.ofSize(DEFAULT_PAGE_SIZE_PLUS_ONE);
        List<Review> reviews = reviewJPARepository.findAllLessThanCursorOrderByCreatedAtDesc(cursorId, cursor, page);
        if (reviews.isEmpty()) {
            throw new CustomException(ErrorCode.REVIEW_LIST_NOT_FOUND);
        }

        boolean hasNext = false;
        int size = reviews.size();
        if (size == DEFAULT_PAGE_SIZE_PLUS_ONE) {
            reviews.remove(size - 1);
            size -= 1;
            hasNext = true;
        }

        Review lastReview = reviews.get(size - 1);

        List<ReviewResponse.RecentReviewDTO> reviewDTOs = new ArrayList<>();
        for (Review review : reviews) {
            String relativeTime = getRelativeTime(review.getCreatedAt());
            reviewDTOs.add(new ReviewResponse.RecentReviewDTO(review, relativeTime));
        }

        return new PageResponse<>(new Paging<>(hasNext, size, lastReview.getCreatedAt(), lastReview.getId()), reviewDTOs);
    }



    private ReviewResponse.FindByReviewIdDTO.ReviewerDTO getReviewerDTO(Review review) {
        User user = userService.findById(review.getUserId());

        return ReviewResponse.FindByReviewIdDTO.ReviewerDTO.builder()
                        .userName(user.getName())
                        .profileImage(user.getProfileImageUrl())
                        .email(user.getEmail())
                        .build();
    }

    //TODO: Locale 이슈
    public String getRelativeTime(LocalDateTime time) {
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