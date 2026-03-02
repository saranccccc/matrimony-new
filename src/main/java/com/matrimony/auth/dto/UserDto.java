package com.matrimony.auth.dto;

import com.matrimony.admin.dto.Role;
import com.matrimony.auth.entity.UserStatus;
import com.matrimony.user.dto.ProfileStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


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
