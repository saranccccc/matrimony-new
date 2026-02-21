package com.matrimony.plan.entity;

import com.matrimony.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "plans")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Plan extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // SILVER, GOLD, PLATINUM

    private String description;

    private BigDecimal price;

    private Integer validityDays; // 90, 180, 365

    private Integer contactViewLimit;

    private Integer messageLimit;

    private Boolean chatEnabled;

    private Boolean profileBoostEnabled;

    private Boolean active; // Admin can disable plan
}
