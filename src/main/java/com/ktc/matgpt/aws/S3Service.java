package com.ktc.matgpt.aws;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.Date;

@RequiredArgsConstructor
@Service
public class S3Service {
    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String s3BucketName;

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

    public void deleteImage(String originalFilename) {
        amazonS3.deleteObject(s3BucketName, originalFilename);
    }
}