package com.matrimony.admin.service;

import com.matrimony.auth.entity.User;
import com.matrimony.auth.entity.UserStatus;
import com.matrimony.auth.repository.UserRepository;
import com.matrimony.common.exception.CustomException;
import com.matrimony.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // =====================================
    // Block User
    // =====================================
    @Transactional
    public void blockUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        user.setUserStatus(UserStatus.BLOCKED);
    }

    // =====================================
    // Optional: Unblock User
    // =====================================
    @Transactional
    public void unblockUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->new CustomException(ErrorCode.USER_NOT_FOUND));
        user.setUserStatus(UserStatus.ACTIVE);
    }

    public User getUser(String userId) {
       return userRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }
}
