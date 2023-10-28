package com.ktc.matgpt.aws;


import org.springframework.web.multipart.MultipartFile;
import java.util.Arrays;
import java.util.List;

public class FileValidator {

    private static final List<String> ALLOWED_FILE_TYPES = Arrays.asList(
            "image/jpeg",
            "image/png",
            "image/heic"  // HEIC 형식 추가
    );
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5 MB

    public static void validateFile(MultipartFile file) throws FileValidationException {
        validateFileType(file);
        validateFileSize(file);
    }

    private static void validateFileType(MultipartFile file) throws FileValidationException {
        if (!ALLOWED_FILE_TYPES.contains(file.getContentType())) {
            throw new FileValidationException("지원하지 않는 파일형태 입니다. 지원하는 파일형태: " + ALLOWED_FILE_TYPES);
        }
    }

    private static void validateFileSize(MultipartFile file) throws FileValidationException {
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new FileValidationException("파일의 최대 등록 사이즈를 넘었습니다. " + (MAX_FILE_SIZE / 1024 / 1024) + " MB");
        }
    }

    public static class FileValidationException extends Exception {
        public FileValidationException(String message) {
            super(message);
        }
    }
}

