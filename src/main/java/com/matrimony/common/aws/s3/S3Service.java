package com.matrimony.common.aws.s3;


import com.matrimony.common.exception.CustomException;
import com.matrimony.common.exception.ErrorCode;
import com.matrimony.common.property.AwsProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class S3Service {
    private final AwsProperties awsProperties;
    private final S3Client s3Client;

    public String createUploadUrl(String userId, MultipartFile file) {
        String contentType = file.getContentType();
        String extension = resolveExtension(file.getContentType());
        //  String extension = extractExtension(Objects.requireNonNull(file.getOriginalFilename()));
        String objectKey = buildS3Key(userId, extension);
        try {
            PutObjectRequest putReq = PutObjectRequest.builder().bucket(awsProperties.getS3().getBucketName()).key(objectKey).contentType(contentType).build();
            PutObjectResponse putObjectResponse = s3Client.putObject(putReq, RequestBody.fromBytes(file.getBytes()));
            if (!putObjectResponse.sdkHttpResponse().isSuccessful()) {
                throw new CustomException(ErrorCode.S3_UPLOAD_FAILED);
            }
            log.info("Photo uploaded to S3. userId={}, key={}, tag={}", userId, objectKey, putObjectResponse.eTag());

        } catch (IOException e) {
            log.error("Failed to upload photo to S3. userId={}", userId, e);
            throw new CustomException(ErrorCode.S3_UPLOAD_FAILED);
        }
        return objectKey;
    }

    private @NonNull String buildS3Key(String userId, String extension) {
        return awsProperties.getS3().getPrefix() + "/" + userId + "/original/" + UUID.randomUUID() + extension;
    }


    private String extractExtension(String originalFilename) {
        int idx = originalFilename.lastIndexOf('.');
        if (idx == -1) {
            return ""; // allow no extension
        }
        return originalFilename.substring(idx).toLowerCase();
    }

    private String resolveExtension(String contentType) {
        if (MediaType.IMAGE_JPEG_VALUE.equals(contentType)) return ".jpg";
        if (MediaType.IMAGE_PNG_VALUE.equals(contentType)) return ".png";
        if ("image/webp".equals(contentType)) return ".webp";
        return "";
    }


    // todo adding public url
/*    private String buildPublicUrl(String key) {
        // If you have CloudFront/CDN base URL, use it
        if (publicBaseUrl != null && !publicBaseUrl.isBlank()) {
            return publicBaseUrl.endsWith("/") ? (publicBaseUrl + key) : (publicBaseUrl + "/" + key);
        }
        // Else fallback to S3 URL (virtual-host style)
        return "https://" + bucketName + ".s3.amazonaws.com/" + key;
    }*/
}