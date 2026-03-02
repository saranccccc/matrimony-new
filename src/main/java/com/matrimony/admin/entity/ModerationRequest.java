package com.matrimony.admin.entity;

import com.matrimony.admin.dto.ModerationAction;
import com.matrimony.admin.dto.ModerationStatus;
import com.matrimony.common.entity.BaseEntity;
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
