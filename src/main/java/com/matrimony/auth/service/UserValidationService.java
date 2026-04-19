package com.matrimony.auth.service;

import com.matrimony.admin.service.UserService;
import com.matrimony.auth.entity.User;
import com.matrimony.auth.entity.UserStatus;
import com.matrimony.common.exception.CustomException;
import com.matrimony.common.exception.ErrorCode;
import com.matrimony.user.dto.ProfileStatus;
import com.matrimony.user.dto.UserProfileResponse;
import com.matrimony.user.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserValidationService {
    private final UserProfileService userProfileService;
    private final UserService userService;
    public boolean validateUser(String userId){
        User user=userService.getUser(userId);
        if (user.getUserStatus() == null || user.getUserStatus() != UserStatus.ACTIVE) {
            throw new CustomException(ErrorCode.USER_NOT_ACTIVE);
        }
        UserProfileResponse profile = userProfileService.getProfile(userId);
        if (profile.getStatus() == null || ProfileStatus.APPROVED != profile.getStatus()) {
            throw new CustomException(ErrorCode.PROFILE_NOT_APPROVED);
        }
        return true;
    }
}
