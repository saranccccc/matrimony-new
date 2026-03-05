package com.matrimony.photo.entity;

import com.matrimony.common.entity.BaseEntity;
import com.matrimony.photo.dto.PhotoStatus;
import com.matrimony.photo.dto.PhotoVisibility;
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
