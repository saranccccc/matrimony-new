package com.matrimony.notification.entity;

import com.matrimony.common.entity.BaseEntity;
import com.matrimony.notification.dto.NotificationType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "notifications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 26, nullable = false)
    private String userId;  // receiver

    @Enumerated(EnumType.STRING)
    private NotificationType type;

    private String title;

    private String message;

    private String referenceId;
    // conversationId / interestId etc

    private Boolean isRead;
}
