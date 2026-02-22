package com.matrimony.access.controller;

import com.matrimony.access.dto.AccessRequestStatus;
import com.matrimony.access.dto.AccessRequestType;
import com.matrimony.access.entity.AccessRequest;
import com.matrimony.access.service.AccessRequestService;
import com.matrimony.auth.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user/access-requests")
@RequiredArgsConstructor
public class AccessRequestController {

    private final AccessRequestService service;

    @PostMapping("/{ownerUserId}")
    public ResponseEntity<?> sendRequest(@AuthenticationPrincipal CustomUserDetails user, @PathVariable String ownerUserId, @RequestParam AccessRequestType type) {
        service.sendRequest(user.getUserId(), ownerUserId, type);
        return ResponseEntity.ok("Access request sent");
    }

    @PutMapping("/{requestId}")
    public ResponseEntity<?> respond(@AuthenticationPrincipal CustomUserDetails user, @PathVariable Long requestId, @RequestParam AccessRequestStatus status) {
        service.respondRequest(requestId, user.getUserId(), status);
        return ResponseEntity.ok("Request updated");
    }

    @GetMapping("/received")
    public Page<AccessRequest> received(@AuthenticationPrincipal CustomUserDetails user, Pageable pageable) {
        return service.getPendingRequests(user.getUserId(), pageable);
    }
}
