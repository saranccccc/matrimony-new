package com.matrimony.plan.service;

import com.matrimony.plan.dto.CreatePlanRequest;
import com.matrimony.plan.dto.PlanResponse;
import com.matrimony.plan.entity.Plan;
import com.matrimony.plan.entity.PlanRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlanService {

    private final PlanRepository planRepository;

    public PlanResponse createPlan(CreatePlanRequest request) {
        log.info("Adding plan:{}", request);
        Plan plan = Plan.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .validityDays(request.getValidityDays())
                .contactViewLimit(request.getContactViewLimit())
                .messageLimit(request.getMessageLimit())
                .chatEnabled(request.getChatEnabled())
                .profileBoostEnabled(request.getProfileBoostEnabled())
                .active(request.getActive())
                .build();
        planRepository.save(plan);
        return mapToResponse(plan);
    }

    public List<PlanResponse> getActivePlans() {
        return planRepository.findByActiveTrue()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private PlanResponse mapToResponse(Plan plan) {
        return PlanResponse.builder()
                .id(plan.getId())
                .name(plan.getName())
                .description(plan.getDescription())
                .price(plan.getPrice())
                .validityDays(plan.getValidityDays())
                .contactViewLimit(plan.getContactViewLimit())
                .messageLimit(plan.getMessageLimit())
                .chatEnabled(plan.getChatEnabled())
                .profileBoostEnabled(plan.getProfileBoostEnabled())
                .build();
    }
}
