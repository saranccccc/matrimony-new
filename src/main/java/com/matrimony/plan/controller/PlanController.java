package com.matrimony.plan.controller;

import com.matrimony.auth.security.CustomUserDetails;
import com.matrimony.plan.dto.CreatePlanRequest;
import com.matrimony.plan.dto.PlanResponse;
import com.matrimony.plan.service.PlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/plans")
@RequiredArgsConstructor
public class PlanController {

    private final PlanService planService;

    @PostMapping
    public ResponseEntity<PlanResponse> createPlan(@AuthenticationPrincipal CustomUserDetails user,@RequestBody CreatePlanRequest request) {
        return ResponseEntity.ok(planService.createPlan(request));
    }

    @GetMapping("/active")
    public ResponseEntity<List<PlanResponse>> getActivePlans(@AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.ok(planService.getActivePlans());
    }
}
