package com.matrimony.interest.entity;

import com.matrimony.common.entity.BaseEntity;
import com.matrimony.interest.dto.InterestStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "interests",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"sender_user_id", "receiver_user_id"}
        ))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Interest extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String senderUserId;

    private String receiverUserId;

    @Enumerated(EnumType.STRING)
    private InterestStatus status;
}
