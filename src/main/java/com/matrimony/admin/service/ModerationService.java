package com.matrimony.admin.service;

import com.matrimony.admin.dto.ModerationAction;
import com.matrimony.admin.dto.ModerationStatus;
import com.matrimony.admin.entity.ModerationRequest;
import com.matrimony.admin.repository.ModerationRepository;
import com.matrimony.photo.dto.PhotoStatus;
import com.matrimony.user.dto.ProfileStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ModerationService {

    private final ModerationRepository moderationRepository;
    private final UserService userService;
    private final PhotoService photoService;

    // ===============================
    // REQUESTER CREATES REQUEST
    // ===============================
    @Transactional
    public void createRequest(
            ModerationAction action,
            String targetUserId,
            Long targetPhotoId,
            String requesterId,
            String remarks) {

        ModerationRequest request =
                ModerationRequest.builder()
                        .action(action)
                        .targetUserId(targetUserId)
                        .targetPhotoId(targetPhotoId)
                        .status(ModerationStatus.PENDING)
                        .requestedBy(requesterId)
                        .remarks(remarks)
                        .build();

        moderationRepository.save(request);
    }

    // ===============================
    // APPROVER GETS PENDING REQUESTS
    // ===============================
    public Page<ModerationRequest> getPendingRequests(
            Pageable pageable) {

        return moderationRepository
                .findByStatus(ModerationStatus.PENDING, pageable);
    }

    // ===============================
    // APPROVE REQUEST
    // ===============================
    @Transactional
    public void approveRequest(
            Long requestId,
            String approverId) {

        ModerationRequest request =
                moderationRepository.findById(requestId)
                        .orElseThrow(() ->
                                new RuntimeException("Request not found"));

        if (request.getStatus() != ModerationStatus.PENDING) {
            throw new RuntimeException("Already processed");
        }

        // Prevent self-approval
        if (request.getRequestedBy().equals(approverId)) {
            throw new RuntimeException("Cannot self-approve request");
        }

        executeAction(request);

        request.setStatus(ModerationStatus.APPROVED);
        request.setApprovedBy(approverId);
    }

    // ===============================
    // REJECT REQUEST
    // ===============================
    @Transactional
    public void rejectRequest(
            Long requestId,
            String approverId) {

        ModerationRequest request =
                moderationRepository.findById(requestId)
                        .orElseThrow(() ->
                                new RuntimeException("Request not found"));

        if (request.getStatus() != ModerationStatus.PENDING) {
            throw new RuntimeException("Already processed");
        }

        if (request.getRequestedBy().equals(approverId)) {
            throw new RuntimeException("Cannot self-reject request");
        }

        request.setStatus(ModerationStatus.REJECTED);
        request.setApprovedBy(approverId);
    }

    // ===============================
    // ACTUAL EXECUTION LOGIC
    // ===============================
    private void executeAction(ModerationRequest request) {

        switch (request.getAction()) {

            case APPROVE_PROFILE -> userService.updateProfileStatus(
                    request.getTargetUserId(),
                    ProfileStatus.APPROVED);

            case REJECT_PROFILE -> userService.updateProfileStatus(
                    request.getTargetUserId(),
                    ProfileStatus.REJECTED);

            case BLOCK_USER -> userService.blockUser(
                    request.getTargetUserId());

            case APPROVE_PHOTO -> photoService.updateStatus(
                    request.getTargetPhotoId(),
                    PhotoStatus.APPROVED);

            case REJECT_PHOTO -> photoService.updateStatus(
                    request.getTargetPhotoId(),
                    PhotoStatus.REJECTED);
        }
    }
}
