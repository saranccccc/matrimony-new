package com.matrimony.auth.dto;

import lombok.Data;

@Data
public class RegisterRequest {

    private String fullName;
    private String mobileNo;
    private String email;

}