package com.matrimony.notification.entity;

import com.matrimony.common.entity.BaseEntity;
import com.matrimony.notification.dto.NotificationType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
