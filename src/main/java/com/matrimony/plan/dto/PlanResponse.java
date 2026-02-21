package com.matrimony.plan.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class PlanResponse {

    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer validityDays;
    private Integer contactViewLimit;
    private Integer messageLimit;
    private Boolean chatEnabled;
    private Boolean profileBoostEnabled;
}
