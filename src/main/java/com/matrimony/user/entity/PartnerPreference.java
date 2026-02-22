package com.matrimony.user.entity;

import com.matrimony.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "partner_preferences")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PartnerPreference extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String userId;

    private Integer minAge;
    private Integer maxAge;

    private Boolean religionNoBar;
    private Boolean casteNoBar;

    private String preferredReligions;
    // comma separated for now (later normalize)

    private String preferredCastes;

    private String preferredCountry;
    private String preferredState;
}
