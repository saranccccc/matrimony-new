package com.matrimony.plan.entity;

import com.matrimony.common.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
