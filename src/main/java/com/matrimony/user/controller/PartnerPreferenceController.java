package com.matrimony.user.controller;

import com.matrimony.auth.security.CustomUserDetails;
import com.matrimony.user.dto.PartnerPreferenceRequest;
import com.matrimony.user.dto.PartnerPreferenceResponse;
import com.matrimony.user.dto.UserProfileResponse;
import com.matrimony.user.service.PartnerPreferenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user/partnerpreference")
@RequiredArgsConstructor
public class PartnerPreferenceController {

    private final PartnerPreferenceService partnerPreferenceService;

    @PostMapping
    public ResponseEntity<?> savePartnerPreference(@AuthenticationPrincipal CustomUserDetails user, @RequestBody PartnerPreferenceRequest request) {
        partnerPreferenceService.createOrUpdatePartnerPreference(user.getUserId(),request);
        return ResponseEntity.ok("PartnerPreference saved/updated successfully");
    }


    @GetMapping
    public ResponseEntity<PartnerPreferenceResponse> getProfile(@AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.ok(partnerPreferenceService.getPartnerPreference(user.getUserId()));
    }

}
