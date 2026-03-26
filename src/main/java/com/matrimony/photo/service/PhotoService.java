package com.matrimony.photo.service;

import com.matrimony.common.exception.CustomException;
import com.matrimony.common.exception.ErrorCode;
import com.matrimony.photo.dto.PhotoStatus;
import com.matrimony.photo.entity.UserPhoto;
import com.matrimony.photo.repository.UserPhotoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PhotoService {
    private final UserPhotoRepository userPhotoRepository;
    public UserPhoto getPhoto(Long photoId) {
        return userPhotoRepository.findById(photoId).orElseThrow(() -> new CustomException(ErrorCode.PHOTO_NOT_FOUND));
    }

    public UserPhoto savePhoto(UserPhoto photo) {
        return userPhotoRepository.save(photo);
    }


    @Transactional
    public void updateStatus(Long photoId, PhotoStatus status) {
        UserPhoto photo = userPhotoRepository.findById(photoId).orElseThrow(() -> new CustomException(ErrorCode.PHOTO_NOT_FOUND));
        photo.setStatus(status);
        log.info("Photo status updated userId={}, status={}", photoId, status);
    }
}
