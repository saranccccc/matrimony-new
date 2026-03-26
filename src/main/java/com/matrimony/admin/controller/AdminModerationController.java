package com.matrimony.admin.controller;

import com.matrimony.admin.dto.ModerationAction;
import com.matrimony.admin.entity.ModerationRequest;
import com.matrimony.admin.service.ModerationService;
import com.matrimony.admin.service.PhotoModerationService;
import com.matrimony.admin.service.ProfileModerationService;
import com.matrimony.auth.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/moderation")
@RequiredArgsConstructor
public class AdminModerationController {
    private final ModerationService moderationService;
    private final ProfileModerationService profileModerationService;
    private final PhotoModerationService photoModerationService;

    // =====================================
    // REQUESTER: Create Moderation Request
    // =====================================
    @PostMapping("/request/profile/{userId}/approve")
    @PreAuthorize("hasRole('ADMIN_REQUESTER')")
    public void requestProfileApproval(@AuthenticationPrincipal CustomUserDetails admin, @PathVariable String userId, @RequestParam(required = false) String remarks) {
        profileModerationService.createRequest(ModerationAction.APPROVE_PROFILE, userId, admin.getUserId(), remarks);
    }

    @PostMapping("/request/profile/{userId}/reject")
    @PreAuthorize("hasRole('ADMIN_REQUESTER')")
    public void requestProfileRejection(@AuthenticationPrincipal CustomUserDetails admin, @PathVariable String userId, @RequestParam(required = false) String remarks) {
        profileModerationService.createRequest(ModerationAction.REJECT_PROFILE, userId, admin.getUserId(), remarks);
    }

    @PostMapping("/request/photo/{photoId}/approve")
    @PreAuthorize("hasRole('ADMIN_REQUESTER')")
    public void requestPhotoApproval(@AuthenticationPrincipal CustomUserDetails admin, @PathVariable Long photoId, @RequestParam(required = false) String remarks) {
        photoModerationService.createRequest(ModerationAction.APPROVE_PHOTO,photoId, admin.getUserId(), remarks);
    }

    @PostMapping("/request/photo/{photoId}/reject")
    @PreAuthorize("hasRole('ADMIN_REQUESTER')")
    public void requestPhotoRejection(@AuthenticationPrincipal CustomUserDetails admin, @PathVariable Long photoId, @RequestParam(required = false) String remarks) {
        photoModerationService.createRequest(ModerationAction.REJECT_PHOTO,photoId, admin.getUserId(), remarks);
    }

    @PostMapping("/request/user/{userId}/block")
    @PreAuthorize("hasRole('ADMIN_REQUESTER')")
    public void requestBlockUser(@AuthenticationPrincipal CustomUserDetails admin, @PathVariable String userId, @RequestParam(required = false) String remarks) {
        profileModerationService.createRequest(ModerationAction.BLOCK_USER, userId, admin.getUserId(), remarks);
    }

    // =====================================
    // APPROVER: View Pending Requests
    // =====================================
    @GetMapping("/pending")
    @PreAuthorize("hasRole('ADMIN_APPROVER')")
    public Page<ModerationRequest> getPendingRequests(Pageable pageable) {
        return moderationService.getPendingRequests(pageable);
    }

    // =====================================
    // APPROVER: Approve
    // =====================================
    @PutMapping("/{requestId}/approve")
    @PreAuthorize("hasRole('ADMIN_APPROVER')")
    public void approveRequest(@AuthenticationPrincipal CustomUserDetails admin, @PathVariable Long requestId) {
        moderationService.approveRequest(requestId, admin.getUserId());
    }

    // =====================================
    // APPROVER: Reject
    // =====================================
    @PutMapping("/{requestId}/reject")
    @PreAuthorize("hasRole('ADMIN_APPROVER')")
    public void rejectRequest(@AuthenticationPrincipal CustomUserDetails admin, @PathVariable Long requestId) {
        moderationService.rejectRequest(requestId, admin.getUserId());
    }
}
