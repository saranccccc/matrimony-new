package com.matrimony.notification.controller;

import com.matrimony.auth.security.CustomUserDetails;
import com.matrimony.notification.entity.Notification;
import com.matrimony.notification.repository.NotificationRepository;
import com.matrimony.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationRepository notificationRepository;
    private final NotificationService notificationService;

    @GetMapping
    public Page<Notification> getNotifications(@AuthenticationPrincipal CustomUserDetails user, Pageable pageable) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(user.getUserId(), pageable);
    }

    @GetMapping("/unread-count")
    public Long getUnreadCount(@AuthenticationPrincipal CustomUserDetails user) {
        return notificationService.getUnreadCount(user.getUserId());
    }

    @PutMapping("/{id}/read")
    public void markAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
    }

    @PutMapping("/read-all")
    public void markAllAsRead(@AuthenticationPrincipal CustomUserDetails user) {
        notificationService.markAllAsRead(user.getUserId());
    }
}
