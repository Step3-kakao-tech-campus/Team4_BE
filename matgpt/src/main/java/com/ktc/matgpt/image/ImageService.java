package com.ktc.matgpt.image;

import com.ktc.matgpt.aws.FileValidator;
import com.ktc.matgpt.review.entity.Review;
import com.ktc.matgpt.aws.S3Service;
import com.ktc.matgpt.tag.TagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@RequiredArgsConstructor
@Service
public class ImageService {
    private final ImageJPARepository imageJPARepository;
    private final TagService tagService;
    private final S3Service s3Service;

    public Image saveImageForReview(Review review, String imageUrl) {
        String imageKey = extractImageKey(imageUrl);

        Image image = Image.create(review, imageKey);
        imageJPARepository.save(image);
        log.info("image-%d: 이미지가 저장되었습니다.", image.getId());
        return image;
    }

    @Transactional
    public void deleteImagesByReviewId(Long reviewId) {
        List<Image> images = imageJPARepository.findAllByReviewId(reviewId);
        if (images.isEmpty()) {
            log.info("review-%d: 해당 리뷰에 삭제할 이미지가 없습니다.", reviewId);
            return;
        }
        deleteImagesAndAssociatedTags(images);
    }

    private String extractImageKey(String imageUrl) {
        Pattern pattern = Pattern.compile("reviews/[\\w-]+/\\d+");
        Matcher matcher = pattern.matcher(imageUrl);

        if (matcher.find()) return matcher.group();
        else throw new NoSuchElementException("이미지 url에 올바른 key가 존재하지 않습니다.");
    }


    private void deleteImagesAndAssociatedTags(List<Image> images) {
        for (Image image : images) {
            tagService.deleteTagsByImageId(image.getId());
            s3Service.deleteImage(image.getUrl());
            imageJPARepository.delete(image);
            log.info("image-%d: 이미지를 삭제했습니다.", image.getId());
        }
    }

    public List<Image> getImagesByReviewId(Long reviewId){
        return imageJPARepository.findAllByReviewId(reviewId);
    }

    public List<String> getImageUrlsByReviewId(Long reviewId){
        return imageJPARepository.findAllImagesByReviewId(reviewId);
    }

    public String getFirstImageByReviewId(Long reviewId){
        List<String> image = imageJPARepository.findFirstImageByReviewId(reviewId, PageRequest.ofSize(1));
        return (image.isEmpty()) ? null : image.get(0);
    }

    @Deprecated
    public void validateImageFile(MultipartFile image) throws FileValidator.FileValidationException {
        FileValidator.validateFile(image);
    }



}
