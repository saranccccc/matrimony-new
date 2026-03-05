package com.matrimony.user.controller;

import com.matrimony.auth.security.CustomUserDetails;
import com.matrimony.user.dto.UserProfileResponse;
import com.matrimony.user.service.MatchEngineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user/matches")
@RequiredArgsConstructor
public class MatchController {
    private final MatchEngineService matchService;
    @GetMapping
    public ResponseEntity<List<UserProfileResponse>> getMatches(@AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.ok(matchService.findMatches(user.getUserId()));
    }
}
