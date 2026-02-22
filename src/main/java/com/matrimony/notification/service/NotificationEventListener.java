package com.matrimony.notification.service;

import com.matrimony.notification.dto.NotificationEvent;
import com.matrimony.notification.entity.Notification;
import com.matrimony.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationEventListener {

    private final NotificationRepository notificationRepository;
    private final PushService pushService;

    @EventListener
    public void handleNotification(NotificationEvent event) {

        Notification notification = Notification.builder()
                .userId(event.getUserId())
                .type(event.getType())
                .title(event.getTitle())
                .message(event.getMessage())
                .referenceId(event.getReferenceId())
                .isRead(false)
                .build();

        notificationRepository.save(notification);

        // Send push
        pushService.sendPush(
                event.getUserId(),
                event.getTitle(),
                event.getMessage()
        );
    }
}
