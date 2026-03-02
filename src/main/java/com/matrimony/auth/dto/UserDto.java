package com.matrimony.auth.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.matrimony.admin.dto.Role;
import com.matrimony.auth.entity.UserStatus;
import com.matrimony.common.entity.BaseEntity;
import com.matrimony.common.util.UlidGenerated;
import com.matrimony.user.dto.ProfileStatus;
import jakarta.persistence.*;
import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class UserDto {

    private String userId;

    private String profileId;

    private String firstName;

    private String lastName;

    private String mobileNo;

    private String email;

    private UserStatus userStatus;

    private ProfileStatus profileStatus;

    private Boolean isBlocked ;

    private Role role;
}
