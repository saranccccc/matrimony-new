package com.matrimony.admin.service;

import com.matrimony.admin.dto.ModerationAction;
import com.matrimony.admin.dto.ModerationStatus;
import com.matrimony.admin.entity.ModerationRequest;
import com.matrimony.admin.repository.ModerationRepository;
import com.matrimony.common.exception.CustomException;
import com.matrimony.common.exception.ErrorCode;
import com.matrimony.photo.dto.PhotoStatus;
import com.matrimony.photo.service.PhotoService;
import com.matrimony.user.dto.ProfileStatus;
import com.matrimony.user.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ModerationService {
    private final ModerationRepository moderationRepository;
    private final UserProfileService userProfileService;
    private final UserService userService;
    private final PhotoService photoService;


    public void findByActionAndTargetUserIdAndStatus(ModerationAction moderationAction,String  targetUserId, ModerationStatus status) {
        moderationRepository.findByActionAndTargetUserIdAndStatus(moderationAction, targetUserId, status).ifPresent(request -> {
            throw new CustomException(ErrorCode.MODERATION_REQUEST_EXISTS);
        });
    }

    public ModerationRequest save(ModerationRequest request ){
     return   moderationRepository.save(request);
    }
    public Page<ModerationRequest> findByStatus(ModerationStatus status, Pageable pageable) {
        return  moderationRepository.findByStatus(status, pageable);
    }

    public ModerationRequest findById(Long id) {
        return moderationRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.MODERATION_REQUEST_NOT_FOUND));
    }

    // ===============================
    // APPROVER GETS PENDING REQUESTS
    // ===============================
    public Page<ModerationRequest> getPendingRequests(Pageable pageable) {
        return findByStatus(ModerationStatus.PENDING, pageable);
    }

    // ===============================
    // APPROVE REQUEST
    // ===============================
    @Transactional
    public void approveRequest(Long requestId, String approverId) {
        ModerationRequest request = getModerationRequest(requestId);
        if (request.getStatus() != ModerationStatus.PENDING) {
            throw new CustomException(ErrorCode.MODERATION_ALREADY_PROCESSED);
        }
        // Prevent self-approval
        if (request.getRequestedBy().equals(approverId)) {
            throw new CustomException(ErrorCode.MODERATION_SELF_APPROVAL_NOT_ALLOWED);
        }
        executeAction(request);
        request.setStatus(ModerationStatus.APPROVED);
        request.setApprovedBy(approverId);
    }

    // ===============================
    // REJECT REQUEST
    // ===============================
    @Transactional
    public void rejectRequest(Long requestId, String approverId) {
        ModerationRequest request = getModerationRequest(requestId);
        if (request.getStatus() != ModerationStatus.PENDING) {
            throw new CustomException(ErrorCode.MODERATION_ALREADY_PROCESSED);
        }
        if (request.getRequestedBy().equals(approverId)) {
            throw new CustomException(ErrorCode.MODERATION_SELF_APPROVAL_NOT_ALLOWED);
        }
        request.setStatus(ModerationStatus.REJECTED);
        request.setApprovedBy(approverId);
    }

    private @NonNull ModerationRequest getModerationRequest(Long requestId) {
        return findById(requestId);
    }

    // ===============================
    // ACTUAL EXECUTION LOGIC
    // ===============================
    private void executeAction(ModerationRequest request) {

        switch (request.getAction()) {

            case APPROVE_PROFILE -> userProfileService.updateStatus(request.getTargetUserId(), ProfileStatus.APPROVED);

            case REJECT_PROFILE -> userProfileService.updateStatus(request.getTargetUserId(), ProfileStatus.REJECTED);

            case BLOCK_USER -> userService.blockUser(request.getTargetUserId());

            case APPROVE_PHOTO -> photoService.updateStatus(request.getTargetPhotoId(), PhotoStatus.APPROVED);

            case REJECT_PHOTO -> photoService.updateStatus(request.getTargetPhotoId(), PhotoStatus.REJECTED);

            default -> throw new CustomException(ErrorCode.INVALID_REQUEST);
        }
    }







}
