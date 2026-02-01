package com.matrimony.auth.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import jakarta.persistence.Id;

@Entity
@Table(name = "user")
@Data
public class User {

    @Id
    private String userId;

    private String fullName;
    private String mobileNo;
    private String email;
    private String username;
    private String passwordHash;
    private String status;
}
