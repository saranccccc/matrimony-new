package com.matrimony.plan.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Data
public class CreatePlanRequest {

    private String name;
    private String description;
    private BigDecimal price;
    private Integer validityDays;
    private Integer contactViewLimit;
    private Integer messageLimit;
    private Boolean chatEnabled;
    private Boolean profileBoostEnabled;
    private Boolean active;
}
