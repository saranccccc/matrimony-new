package com.matrimony.photo.service;

import com.matrimony.common.exception.CustomException;
import com.matrimony.common.exception.ErrorCode;
import com.matrimony.photo.dto.PhotoStatus;
import com.matrimony.photo.entity.UserPhoto;
import com.matrimony.photo.repository.UserPhotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminPhotoService {

    private final UserPhotoRepository photoRepository;

    @Transactional
    public void updatePhotoStatus(Long photoId, PhotoStatus status) {
        UserPhoto photo = photoRepository.findById(photoId).orElseThrow(() -> new CustomException(ErrorCode.PHOTO_NOT_FOUND));
        photo.setStatus(status);
        photoRepository.save(photo); // Explicit save for clarity
    }

    @Transactional
    public void updateStatus(Long photoId, PhotoStatus status) {
        UserPhoto photo = photoRepository.findById(photoId).orElseThrow(() -> new RuntimeException("Photo not found"));
        photo.setStatus(status);
    }
}
