package com.matrimony.pub;

import com.matrimony.plan.dto.PlanResponse;
import com.matrimony.plan.service.PlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pub")
@RequiredArgsConstructor
public class PublicController {
    private final PlanService planService;

    @GetMapping("/plans/active")
    public ResponseEntity<List<PlanResponse>> getActivePlans() {
        return ResponseEntity.ok(planService.getActivePlans());
    }
}
