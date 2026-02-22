package com.matrimony.interest.controller;

import com.matrimony.auth.security.CustomUserDetails;
import com.matrimony.interest.dto.InterestStatus;
import com.matrimony.interest.entity.Interest;
import com.matrimony.interest.service.InterestService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user/interests")
@RequiredArgsConstructor
public class InterestController {

    private final InterestService interestService;

    @PostMapping("/{receiverUserId}")
    public ResponseEntity<?> sendInterest(@AuthenticationPrincipal CustomUserDetails user, @PathVariable String receiverUserId) {
        interestService.sendInterest(user.getUserId(), receiverUserId);
        return ResponseEntity.ok("Interest sent successfully");
    }

    @PutMapping("/{interestId}")
    public ResponseEntity<?> respondInterest(@AuthenticationPrincipal CustomUserDetails user, @PathVariable Long interestId, @RequestParam InterestStatus status) {
        interestService.respondToInterest(interestId, user.getUserId(), status);
        return ResponseEntity.ok("Interest updated");
    }

    @GetMapping("/received")
    public Page<Interest> received(@AuthenticationPrincipal CustomUserDetails user, Pageable pageable) {
        return interestService.getReceivedInterests(user.getUserId(), pageable);
    }

    @GetMapping("/sent")
    public Page<Interest> sent(@AuthenticationPrincipal CustomUserDetails user, Pageable pageable) {
        return interestService.getSentInterests(user.getUserId(), pageable);
    }
}
