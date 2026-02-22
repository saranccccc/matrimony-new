package com.matrimony.photo.service;

import com.matrimony.common.exception.CustomException;
import com.matrimony.common.exception.ErrorCode;
import com.matrimony.photo.dto.PhotoStatus;
import com.matrimony.photo.dto.PhotoVisibility;
import com.matrimony.photo.entity.UserPhoto;
import com.matrimony.photo.repository.UserPhotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserPhotoService {

    private final UserPhotoRepository photoRepository;

    @Transactional
    public void uploadPhoto(String userId, String photoUrl, PhotoVisibility visibility) {
        UserPhoto photo = UserPhoto.builder().userId(userId).photoUrl(photoUrl).isPrimary(false).status(PhotoStatus.PENDING_APPROVAL).visibility(visibility).isDeleted(false).build();
        photoRepository.save(photo);
    }

    @Transactional
    public void setPrimaryPhoto(String userId, Long photoId) {
        List<UserPhoto> photos = photoRepository.findByUserIdAndIsDeletedFalse(userId);
        for (UserPhoto p : photos) {
            p.setIsPrimary(p.getId().equals(photoId));
        }
    }

    @Transactional
    public void deletePhoto(String userId, Long photoId) {
        UserPhoto photo = photoRepository.findById(photoId).orElseThrow(() -> new CustomException(ErrorCode.PHOTO_NOT_FOUND));
        if (!photo.getUserId().equals(userId)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }
        photo.setIsDeleted(true);
    }

    public List<UserPhoto> getUserPhotos(String userId) {
        return photoRepository.findByUserIdAndIsDeletedFalse(userId);
    }
}
