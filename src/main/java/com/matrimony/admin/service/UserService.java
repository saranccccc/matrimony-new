package com.matrimony.admin.service;

import com.matrimony.auth.entity.User;
import com.matrimony.auth.entity.UserStatus;
import com.matrimony.auth.repository.UserRepository;
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
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        user.setUserStatus(UserStatus.BLOCKED);
    }

    // =====================================
    // Optional: Unblock User
    // =====================================
    @Transactional
    public void unblockUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        user.setUserStatus(UserStatus.ACTIVE);
    }
}
