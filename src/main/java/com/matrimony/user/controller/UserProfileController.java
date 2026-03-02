package com.matrimony.user.controller;

import com.matrimony.auth.security.CustomUserDetails;
import com.matrimony.user.dto.UserProfileRequest;
import com.matrimony.user.dto.UserProfileResponse;
import com.matrimony.user.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user/profile")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileService profileService;

    @PostMapping
    public ResponseEntity<?> saveProfile(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestBody UserProfileRequest request) {

        profileService.createOrUpdateProfile(
                user.getUserId(), request);

        return ResponseEntity.ok("Profile saved successfully");
    }

    @GetMapping
    public ResponseEntity<UserProfileResponse> getProfile(
            @AuthenticationPrincipal CustomUserDetails user) {

        return ResponseEntity.ok(
                profileService.getProfile(user.getUserId()));
    }
}
