package com.matrimony.auth.entity;

import com.matrimony.admin.dto.Role;
import com.matrimony.common.entity.BaseEntity;
import com.matrimony.common.util.UlidGenerated;
import com.matrimony.user.dto.ProfileStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseEntity {

    @Id
    @UlidGenerated
    @Column(name = "user_id", length = 26, nullable = false, updatable = false)
    private String userId;

    @Column(unique = true, nullable = false)
    private String profileId;

    @Column(unique = true, nullable = false)
    private String firstName;

    private String lastName;

    @Column(unique = true, nullable = false)
    private String mobileNo;

    @Column(unique = true)
    private String email;

    private String passwordHash;

    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;

    @Enumerated(EnumType.STRING)
    private ProfileStatus profileStatus;

    @Column(name = "is_blocked", nullable = false)
    private Boolean isBlocked = Boolean.FALSE;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.USER;
}
