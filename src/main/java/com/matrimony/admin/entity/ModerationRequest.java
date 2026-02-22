package com.matrimony.admin.entity;

import com.matrimony.admin.dto.ModerationAction;
import com.matrimony.admin.dto.ModerationStatus;
import com.matrimony.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "moderation_requests")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ModerationRequest extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ModerationAction action;

    private String targetUserId; // ULID

    private Long targetPhotoId; // nullable

    @Enumerated(EnumType.STRING)
    private ModerationStatus status;

    private String requestedBy; // admin ULID

    private String approvedBy; // nullable

    private String remarks;
}
