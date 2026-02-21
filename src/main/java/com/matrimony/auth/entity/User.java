package com.matrimony.auth.entity;

import com.matrimony.common.entity.BaseEntity;
import com.matrimony.common.util.UlidGenerated;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseEntity {


/*    @Id
    @GeneratedValue
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    @Column(columnDefinition = "BINARY(16)")
    private UUID userId;*/

    @Id
    @UlidGenerated
    @Column(name = "user_id", length = 26, nullable = false, updatable = false)
    private String userId;

    @Column(unique = true, nullable = false)
    private String profileId;

    @Column(unique = true, nullable = false)
    private String firstName;

    private String lastName;

    @Column(unique = true, nullable = false)
    private String mobileNo;

    @Column(unique = true)
    private String email;

    private String passwordHash;

    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;


}
