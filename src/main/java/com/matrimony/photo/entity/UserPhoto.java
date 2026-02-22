package com.matrimony.photo.entity;

import com.matrimony.common.entity.BaseEntity;
import com.matrimony.photo.dto.PhotoStatus;
import com.matrimony.photo.dto.PhotoVisibility;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_photos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserPhoto extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", length = 26, nullable = false)
    private String userId;

    private String photoUrl;

    private Boolean isPrimary;

    @Enumerated(EnumType.STRING)
    private PhotoStatus status;

    @Enumerated(EnumType.STRING)
    private PhotoVisibility visibility;

    private Boolean isDeleted;
}
