package com.matrimony.chat.entity;

import com.matrimony.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "conversations", uniqueConstraints = @UniqueConstraint(columnNames = {"user1_id", "user2_id"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Conversation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user1_id", length = 26, nullable = false)
    private String user1Id;

    @Column(name = "user2_id", length = 26, nullable = false)
    private String user2Id;

    private Boolean isBlocked;
}
