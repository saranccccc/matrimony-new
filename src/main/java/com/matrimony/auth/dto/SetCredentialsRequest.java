package com.matrimony.auth.dto;

import lombok.Data;

@Data
public class SetCredentialsRequest {

    private String userId;
    private String username;
    private String password;


}
