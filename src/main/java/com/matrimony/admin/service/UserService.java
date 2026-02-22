package com.matrimony.admin.service;

import com.matrimony.auth.entity.User;
import com.matrimony.auth.repository.UserRepository;
import com.matrimony.user.dto.ProfileStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // =====================================
    // Update Profile Status
    // =====================================
    @Transactional
    public void updateProfileStatus(String userId, ProfileStatus status) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        user.setProfileStatus(status);
    }

    // =====================================
    // Block User
    // =====================================
    @Transactional
    public void blockUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        user.setIsBlocked(true);
    }

    // =====================================
    // Optional: Unblock User
    // =====================================
    @Transactional
    public void unblockUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        user.setIsBlocked(false);
    }
}
