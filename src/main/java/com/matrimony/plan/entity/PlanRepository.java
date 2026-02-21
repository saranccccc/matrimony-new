package com.matrimony.plan.entity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PlanRepository extends JpaRepository<Plan, Long> {

    List<Plan> findByActiveTrue();

    Optional<Plan> findByIdAndActiveTrue(Long id);
}
