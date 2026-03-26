package com.matrimony.admin.service;

import com.matrimony.admin.dto.ModerationAction;
import com.matrimony.admin.dto.ModerationStatus;
import com.matrimony.admin.entity.ModerationRequest;
import com.matrimony.admin.repository.ModerationRepository;
import com.matrimony.common.exception.CustomException;
import com.matrimony.common.exception.ErrorCode;
import com.matrimony.photo.dto.PhotoStatus;
import com.matrimony.photo.entity.UserPhoto;
import com.matrimony.photo.service.PhotoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PhotoModerationService {
    private final ModerationRepository moderationRepository;
    private final PhotoService photoService;

    public void createRequest(ModerationAction action, Long photoId, String requesterId, String remarks) {
        UserPhoto photo = photoService.getPhoto(photoId);
        if (photo.getStatus() != PhotoStatus.UPLOADED ) {
            throw new CustomException(ErrorCode.MODERATION_REQUEST_NOT_VALID);
        }

        moderationRepository.findByActionAndTargetPhotoIdAndStatus(action, photoId, ModerationStatus.PENDING).ifPresent(request -> {
            throw new CustomException(ErrorCode.MODERATION_REQUEST_EXISTS);
        });

        photoService.updateStatus(photoId,PhotoStatus.PENDING_APPROVAL);

        ModerationRequest request = ModerationRequest.builder().action(action).targetPhotoId(photoId).targetUserId(photo.getUserId()).status(ModerationStatus.PENDING).requestedBy(requesterId).remarks(remarks).build();

        moderationRepository.save(request);

        log.info("Photo approval request raised successfully. photoId={}, requesterId={}", photoId, requesterId);
    }



}
