package com.matrimony.user.entity;

import com.matrimony.common.entity.BaseEntity;
import com.matrimony.user.dto.ProfileStatus;
import com.matrimony.user.dto.VisibilityLevel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfile extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String userId;

    private String firstName;
    private String lastName;

    private Integer age;
    private String gender;

    private String religion;
    private String caste;
    private String subCaste;

    private String education;
    private String profession;

    private String city;
    private String state;
    private String country;

    private String aboutMe;

    @Enumerated(EnumType.STRING)
    private ProfileStatus status;

    @Enumerated(EnumType.STRING)
    private VisibilityLevel photoVisibility;

    @Enumerated(EnumType.STRING)
    private VisibilityLevel contactVisibility;

    private Boolean profileCompleted;

    private Boolean isBoosted;
    private LocalDateTime boostExpiry;
}
