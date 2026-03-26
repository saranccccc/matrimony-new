package com.matrimony.admin.service;

import com.matrimony.admin.dto.ModerationAction;
import com.matrimony.admin.dto.ModerationStatus;
import com.matrimony.admin.entity.ModerationRequest;
import com.matrimony.common.exception.CustomException;
import com.matrimony.common.exception.ErrorCode;
import com.matrimony.user.dto.ProfileStatus;
import com.matrimony.user.dto.UserProfileResponse;
import com.matrimony.user.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProfileModerationService {


    private final UserProfileService userProfileService;
    private final UserService userService;
    private final ModerationService moderationService;

    // ===============================
    // REQUESTER CREATES REQUEST
    // ===============================
    @Transactional
    public void createRequest(ModerationAction action, String targetUserId, String requesterId, String remarks) {
        UserProfileResponse profile = userProfileService.getProfile(targetUserId);

        if (ProfileStatus.COMPLETED != profile.getStatus()) {
            throw new CustomException(ErrorCode.PROFILE_NOT_COMPLETED);
        }
        moderationService.findByActionAndTargetUserIdAndStatus(action, targetUserId, ModerationStatus.PENDING);

        userProfileService.updateStatus(targetUserId, ProfileStatus.UNDER_REVIEW);
        ModerationRequest request = ModerationRequest.builder().action(action).targetUserId(targetUserId).status(ModerationStatus.PENDING).requestedBy(requesterId).remarks(remarks).build();
        moderationService.save(request);
    }

}
