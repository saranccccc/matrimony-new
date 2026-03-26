package com.matrimony.photo.service;

import com.matrimony.common.aws.s3.S3Service;
import com.matrimony.common.exception.CustomException;
import com.matrimony.common.exception.ErrorCode;
import com.matrimony.photo.dto.PhotoResponse;
import com.matrimony.photo.dto.PhotoStatus;
import com.matrimony.photo.dto.PhotoVisibility;
import com.matrimony.photo.entity.UserPhoto;
import com.matrimony.photo.property.PhotoProperties;
import com.matrimony.photo.repository.UserPhotoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserPhotoService {

    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of(MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE, "image/webp");
    private final UserPhotoRepository photoRepository;
    private final S3Service s3Service;
    private final PhotoProperties photoProperties;

    @Transactional
    public PhotoResponse uploadPhotoToS3(String userId, MultipartFile file, PhotoVisibility visibility) {
        validateUpload(file);
        long count = photoRepository.countByUserId(userId);
        if (count >= photoProperties.getMaxCount()) {
            throw new CustomException(ErrorCode.PHOTO_LIMIT_REACHED);
        }
        String photoUrl = s3Service.createUploadUrl(userId, file);
        UserPhoto photo = uploadPhoto(userId, photoUrl, visibility, count);
        log.info("UserPhoto saved. userId={}, photoId={}", userId, photo.getId());
        return PhotoResponse.builder().photoId(photo.getId()).build();
    }

    @Transactional
    public PhotoResponse setPrimaryPhoto(String userId, Long photoId) {
        List<UserPhoto> photos = photoRepository.findByUserIdAndIsDeletedFalse(userId);
        boolean found = false;
        PhotoResponse photoResponse = null;

        // todo: Optional: only allow primary if APPROVED
        // if (photo.getStatus() != PhotoStatus.APPROVED) {
        //     throw new CustomException("PHOTO_NOT_APPROVED", "Only approved photos can be set as primary.");
        // }

        for (UserPhoto p : photos) {
            // todo check isdeleted
            boolean isPrimary = p.getId().equals(photoId);
            p.setIsPrimary(isPrimary);
            photoResponse = PhotoResponse.builder().photoId(p.getId()).isPrimary(true).build();
            if (isPrimary) {
                found = true;
            }
        }
        if (!found) {
            throw new CustomException(ErrorCode.PHOTO_NOT_FOUND);
        }
        log.info("Primary photo set. userId={}, photoId={}", userId, photoId);
        return photoResponse;
    }

    @Transactional
    public PhotoResponse deletePhoto(String userId, Long photoId) {
        UserPhoto photo = photoRepository.findById(photoId).orElseThrow(() -> new CustomException(ErrorCode.PHOTO_NOT_FOUND));
        if (!photo.getUserId().equals(userId)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }
        if (Boolean.TRUE == photo.getIsPrimary()) {
            throw new CustomException(ErrorCode.INVALID_OPERATION_PHOTO);
        }
        photo.setIsDeleted(true);
        log.info("Photo soft-deleted. userId={}, photoId={}", userId, photoId);
        return PhotoResponse.builder().photoId(photo.getId()).isPrimary(photo.getIsPrimary()).isDeleted(true).build();
    }


    public List<UserPhoto> getUserPhotos(String userId) {
        return photoRepository.findByUserIdAndIsDeletedFalse(userId);
    }

    @Transactional
    public UserPhoto uploadPhoto(String userId, String photoUrl, PhotoVisibility visibility, Long count) {
        UserPhoto photo = UserPhoto.builder().userId(userId).photoUrl(photoUrl).isPrimary(count == 0).status(PhotoStatus.UPLOADED).visibility(visibility).isDeleted(false).build();
        return photoRepository.save(photo);
    }

    private void validateUpload(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new CustomException(ErrorCode.INVALID_PHOTO_REQUEST);
        }
        if (file.getContentType() == null || !(file.getContentType().startsWith("image/"))) {
            throw new CustomException(ErrorCode.INVALID_FILE_TYPE);
        }
        if (file.getSize() > photoProperties.getMaxFileSize()) {
            throw new CustomException(ErrorCode.PHOTO_TOO_LARGE);
        }
        if (!ALLOWED_CONTENT_TYPES.contains(file.getContentType())) {
            throw new CustomException(ErrorCode.INVALID_PHOTO_TYPE);
        }
    }

    @Transactional
    public void updateStatus(Long photoId, PhotoStatus status) {
        UserPhoto photo = photoRepository.findById(photoId).orElseThrow(() -> new CustomException(ErrorCode.PHOTO_NOT_FOUND));
        photo.setStatus(status);
        log.info("Photo status updated photoId={}, status={}", photoId, status);
    }
}
