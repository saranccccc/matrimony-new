package com.matrimony.plan.controller;

import com.matrimony.plan.dto.CreatePlanRequest;
import com.matrimony.plan.dto.PlanResponse;
import com.matrimony.plan.service.PlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/plans")
@RequiredArgsConstructor
public class PlanController {

    private final PlanService planService;

    @PostMapping
    public ResponseEntity<PlanResponse> createPlan(
            @RequestBody CreatePlanRequest request) {
        return ResponseEntity.ok(planService.createPlan(request));
    }

    @GetMapping("/active")
    public ResponseEntity<List<PlanResponse>> getActivePlans() {
        return ResponseEntity.ok(planService.getActivePlans());
    }
}
