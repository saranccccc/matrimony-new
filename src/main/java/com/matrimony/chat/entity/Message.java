package com.matrimony.chat.entity;

import com.matrimony.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "messages")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Message extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long conversationId;

    @Column(length = 26)
    private String senderUserId;

    @Column(columnDefinition = "TEXT")
    private String content;

    private Boolean isRead;
}
