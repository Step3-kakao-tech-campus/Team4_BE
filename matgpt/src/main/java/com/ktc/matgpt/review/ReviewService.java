package com.ktc.matgpt.review;

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
import com.ktc.matgpt.security.UserPrincipal;
import com.ktc.matgpt.tag.TagService;
import com.ktc.matgpt.store.Store;
import com.ktc.matgpt.store.StoreService;
import com.ktc.matgpt.user.entity.User;
import com.ktc.matgpt.user.service.UserService;
import com.ktc.matgpt.utils.CursorRequest;
import com.ktc.matgpt.utils.PageResponse;
import com.ktc.matgpt.utils.Paging;
import com.ktc.matgpt.utils.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URL;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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

    private static final int DEFAULT_PAGE_SIZE = 8;
    private static final int DEFAULT_PAGE_SIZE_PLUS_ONE = DEFAULT_PAGE_SIZE + 1;
    private static final PageResponse EMPTY_PAGE_RESPONSE = new PageResponse<>(new Paging<>(false, 0, null, null), null);

    @Transactional
    public void completeReviewUpload(Long storeId, Long reviewId, ReviewRequest.CreateCompleteDTO requestDTO, String userEmail) {
        // 이미지 업로드 완료 후 리뷰, 이미지, 태그 정보 저장 로직
        // 이 로직은 이미지 업로드가 완료된 후 호출됩니다.

        //Store 프록시객체
        Store storeRef = storeService.getReferenceById(storeId);
        Review review = findReviewByIdOrThrow(reviewId);
        if (!userEmail.equals(review.getUser().getEmail())) {
            throw new CustomException(ErrorCode.REVIEW_UNAUTHORIZED_ACCESS);
        }

        for (ReviewRequest.CreateCompleteDTO.ImageDTO imageDTO : requestDTO.getReviewImages()) {
            Image image = imageService.saveImageForReview(review, imageDTO.getImageUrl()); // 이미지 생성 및 리뷰에 매핑하여 저장
            saveTagsForImage(image, imageDTO, storeRef); // 태그 저장
        }
    }

    // 리뷰 생성 메서드
    public Review createTemporaryReview(String userEmail, Long storeId, ReviewRequest.SimpleCreateDTO simpleDTO) {
        User userRef = userService.getReferenceByEmail(userEmail);

        //Store 리뷰 개수 및 평점 업데이트
        Store store = storeService.findById(storeId);
        if (simpleDTO.getPeopleCount() == 0) {
            throw new CustomException(ErrorCode.REVIEW_INVALID_PEOPLE_COUNT);
        }
        int costPerPerson = simpleDTO.getTotalPrice() / simpleDTO.getPeopleCount();
        store.addReview(simpleDTO.getRating(), simpleDTO.getPeopleCount(), costPerPerson);

        // 리뷰 데이터 저장
        Review review = Review.create(userRef, store, simpleDTO.getContent(), simpleDTO.getRating(), simpleDTO.getPeopleCount(), simpleDTO.getTotalPrice());
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

    @Transactional
    public void updateContent(Long reviewId, String userEmail, ReviewRequest.UpdateDTO requestDTO) {
        Review review = findReviewByIdOrThrow(reviewId);
        if (!userEmail.equals(review.getUser().getEmail())) {
            throw new CustomException(ErrorCode.REVIEW_UNAUTHORIZED_ACCESS);
        }
        review.updateContent(requestDTO.getContent());
    }

    @Transactional(readOnly = true)
    public ReviewResponse.FindByReviewIdDTO findDetailByReviewId(Long reviewId, UserPrincipal userPrincipal) {
        Review review = findReviewByIdOrThrow(reviewId);
        boolean isOwner = userPrincipal != null && userPrincipal.getEmail().equals(review.getUser().getEmail());

        ReviewResponse.FindByReviewIdDTO.ReviewerDTO reviewerDTO = getReviewerDTO(review);

        List<ReviewResponse.FindByReviewIdDTO.ImageDTO> imageDTOs = imageService.getImagesByReviewId(reviewId).stream()
                .map(image -> new ReviewResponse.FindByReviewIdDTO.ImageDTO(
                        image, tagService.getTagsByImageId(image.getId())))
                .toList();

        String relativeTime = getRelativeTime(review.getCreatedAt());
        return new ReviewResponse.FindByReviewIdDTO(review, reviewerDTO, imageDTOs, relativeTime, isOwner);
    }

    public PageResponse<?, ReviewResponse.StoreReviewDTO> findPageByStoreId(Long storeId, String sortBy, Long cursorId, Integer cursor) {
        CursorRequest<Integer> page = new CursorRequest(DEFAULT_PAGE_SIZE_PLUS_ONE, cursor, Integer.class, cursorId);

        List<Review> reviews = switch (sortBy) {
            case "latest" -> reviewJPARepository.findAllByStoreIdAndOrderByIdDesc(storeId, page.cursorId, page.request);
            case "likes" -> reviewJPARepository.findAllByStoreIdAndOrderByLikesAndIdDesc(storeId, page.cursorId, page.cursor, page.request);
            default -> throw new CustomException(ErrorCode.INVALID_SORT_TYPE, sortBy);
        };
        if (reviews.isEmpty()) return EMPTY_PAGE_RESPONSE;

        Paging<Integer> paging = getPagingInfo(reviews);
        return new PageResponse<>(paging, getStoreReviewDTOs(reviews.subList(0, paging.size())));
    }

    private List<ReviewResponse.StoreReviewDTO> getStoreReviewDTOs(List<Review> reviewList) {
        return reviewList.stream().map(review -> {
            String image = imageService.getFirstImageByReviewId(review.getId());
            String relativeTime = getRelativeTime(review.getCreatedAt());
            return new ReviewResponse.StoreReviewDTO(review, relativeTime, image);
        }).collect(Collectors.toList());
    }

    public List<Review> findByStoreIdAndSummaryType(Long storeId, String summaryType, int limit) {
        Pageable pageable = switch (summaryType) {
            case "best" -> PageRequest.of(0, limit, Sort.by(Sort.Order.desc("rating")));
            case "worst" -> PageRequest.of(0, limit, Sort.by(Sort.Order.asc("rating")));
            default -> throw new CustomException(ErrorCode.REVIEW_INVALID_SUMMARY_TYPE, summaryType);
        };

        return reviewJPARepository.findByStoreId(storeId, pageable);
    }

    public PageResponse<?, ReviewResponse.UserReviewDTO> findPageByUserId(String userEmail, String sortBy, Long cursorId, Integer cursor) {
        User userRef = userService.getReferenceByEmail(userEmail);
        CursorRequest<Integer> page = new CursorRequest(DEFAULT_PAGE_SIZE_PLUS_ONE, cursor, Integer.class, cursorId);

        List<Review> reviewList = switch (sortBy) {
            case "latest" -> reviewJPARepository.findAllByUserIdAndOrderByIdDesc(userRef.getId(), page.cursorId, page.request);
            case "likes" -> reviewJPARepository.findAllByUserIdAndOrderByLikesAndIdDesc(userRef.getId(), page.cursorId, page.cursor, page.request);
            default -> throw new CustomException(ErrorCode.INVALID_SORT_TYPE, sortBy);
        };
        if (reviewList.isEmpty()) return EMPTY_PAGE_RESPONSE;

        Paging<Integer> paging = getPagingInfo(reviewList);
        return new PageResponse<>(paging, getUserReviewDTOs(reviewList.subList(0, paging.size())));
    }

    private List<ReviewResponse.UserReviewDTO> getUserReviewDTOs(List<Review> reviewList) {
        return reviewList.stream().map(review -> {
            String relativeTime = getRelativeTime(review.getCreatedAt());
            return new ReviewResponse.UserReviewDTO(review, relativeTime);
        }).collect(Collectors.toList());
    }


    @Transactional
    public void delete(Long reviewId, String userEmail) {
        Review review = findReviewByIdOrThrow(reviewId);
        if (!userEmail.equals(review.getUser().getEmail())) {
            throw new CustomException(ErrorCode.REVIEW_UNAUTHORIZED_ACCESS);
        }
        imageService.deleteImagesByReviewId(reviewId);

        Store store = storeService.findById(review.getStore().getId());
        store.removeReview(review.getRating(), review.getPeopleCount(), review.getCostPerPerson());

        likeReviewService.deleteAllByReviewId(reviewId);
        reviewJPARepository.deleteById(reviewId);
        log.info("review-%d: 리뷰가 삭제되었습니다.", review.getId());
    }

    @Transactional(readOnly = true)
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

    private void saveTagsForImage(Image image, ReviewRequest.CreateCompleteDTO.ImageDTO imageDTO, Store store) {
        for (ReviewRequest.CreateCompleteDTO.ImageDTO.TagDTO tagDTO : imageDTO.getTags()) {
            Food food = foodService.saveOrUpdateFoodByTagName(tagDTO, store.getId()); //TODO: 태그에 들어갈 음식 검색하고 추가하는 로직
            tagService.saveTag(image, food, tagDTO);
        }
    }

    private Paging<Integer> getPagingInfo(List<Review> reviews) {
        boolean hasNext = false;
        int numsOfReviews = reviews.size();

        if (numsOfReviews == DEFAULT_PAGE_SIZE_PLUS_ONE) {
            numsOfReviews -= 1;
            hasNext = true;
        }

        Review lastReview = reviews.get(numsOfReviews-1);
        Integer nextCursor = lastReview.getRecommendCount();
        Long nextCursorId = lastReview.getId();

        return new Paging<Integer>(hasNext, numsOfReviews, nextCursor, nextCursorId);
    }


    private ReviewResponse.FindByReviewIdDTO.ReviewerDTO getReviewerDTO(Review review) {
        User user = userService.findByEmail(review.getUser().getEmail());

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
        return value + " " + unitKey + " ago";
        //        String timeUnitMessage = messageSourceAccessor.getMessage(unitKey, locale);
        //        return value + " " + timeUnitMessage + " " + messageSourceAccessor.getMessage("ago", locale);
    }
}