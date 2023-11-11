package com.ktc.matgpt.domain.aws;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.ktc.matgpt.exception.ErrorMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Service
public class S3Service {
    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String s3BucketName;
    private static Pattern reviewPattern = Pattern.compile("https://matgpt-prod\\.s3\\.ap-northeast-2\\.amazonaws\\.com/reviews/[\\w-]+/\\d+");
    private static Pattern userPattern = Pattern.compile("https://matgpt-prod\\.s3\\.ap-northeast-2\\.amazonaws\\.com/users/[^?]+");
    private static Pattern keyPatten = Pattern.compile("reviews/[\\w-]+/\\d+");

    public URL getPresignedUrl(String objectKey) {
        Date expiration = new Date();
        long expTimeMillis = expiration.getTime() + 1000 * 60 * 60;
        expiration.setTime(expTimeMillis);

        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(s3BucketName, objectKey)
                    .withMethod(HttpMethod.PUT)
                    .withExpiration(expiration);

        return amazonS3.generatePresignedUrl(generatePresignedUrlRequest);
    }

    public String getReviewImageUrl(String presignedUrl) {
        Matcher matcher = reviewPattern.matcher(presignedUrl);
        if (!matcher.find()) throw new NoSuchElementException(ErrorMessage.INVALID_S3_URL);

        String s3Url = matcher.group();
        return s3Url;
    }

    public String getUserImageUrl(String presignedUrl) {
        Matcher matcher = userPattern.matcher(presignedUrl);
        if (!matcher.find()) throw new NoSuchElementException(ErrorMessage.INVALID_S3_URL);

        String s3Url = matcher.group();
        return s3Url;
    }

    public void deleteImage(String s3Url) {
        String s3Key = getKeyFromS3Url(s3Url);
        amazonS3.deleteObject(new DeleteObjectRequest(s3BucketName, s3Key));
    }

    private String getKeyFromS3Url(String s3Url) {
        Matcher matcher = keyPatten.matcher(s3Url);
        if (!matcher.find()) throw new NoSuchElementException(ErrorMessage.INVALID_S3_URL);

        String s3Key = matcher.group();
        return s3Key;
    }
}