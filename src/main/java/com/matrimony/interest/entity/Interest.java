package com.matrimony.interest.entity;

import com.matrimony.common.entity.BaseEntity;
import com.matrimony.interest.dto.InterestStatus;
import jakarta.persistence.*;
import lombok.*;

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
