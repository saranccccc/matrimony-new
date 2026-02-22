package com.matrimony.notification.service;

import com.matrimony.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    @Transactional
    public void markAsRead(Long id) {
        notificationRepository.markAsRead(id);
    }

    @Transactional
    public void markAllAsRead(String userId) {
        notificationRepository.markAllAsRead(userId);
    }

    public Long getUnreadCount(String userId) {
        return notificationRepository.countByUserIdAndIsReadFalse(userId);
    }
}
