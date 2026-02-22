package com.matrimony.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NotificationEvent {

    private String userId;
    private NotificationType type;
    private String title;
    private String message;
    private String referenceId;
}
