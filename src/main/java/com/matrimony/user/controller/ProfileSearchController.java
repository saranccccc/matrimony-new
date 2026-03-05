package com.matrimony.user.controller;

import com.matrimony.auth.security.CustomUserDetails;
import com.matrimony.user.dto.ProfileSearchRequest;
import com.matrimony.user.dto.UserProfileResponse;
import com.matrimony.user.service.ProfileSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user/search")
@RequiredArgsConstructor
public class ProfileSearchController {
    private final ProfileSearchService searchService;

    @PostMapping
    public ResponseEntity<Page<UserProfileResponse>> search(@AuthenticationPrincipal CustomUserDetails user, @RequestBody ProfileSearchRequest request, Pageable pageable) {
        return ResponseEntity.ok(searchService.search(user.getUserId(), request, pageable));
    }
}
