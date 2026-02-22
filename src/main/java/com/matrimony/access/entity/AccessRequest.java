package com.matrimony.access.entity;

import com.matrimony.access.dto.AccessRequestStatus;
import com.matrimony.access.dto.AccessRequestType;
import com.matrimony.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "access_requests", uniqueConstraints = @UniqueConstraint(columnNames = {"requester_user_id", "owner_user_id", "type"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccessRequest extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "requester_user_id", length = 26, nullable = false)
    private String requesterUserId;

    @Column(name = "owner_user_id", length = 26, nullable = false)
    private String ownerUserId;

    @Enumerated(EnumType.STRING)
    private AccessRequestType type;

    @Enumerated(EnumType.STRING)
    private AccessRequestStatus status;
}
