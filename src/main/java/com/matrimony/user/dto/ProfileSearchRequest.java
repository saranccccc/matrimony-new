package com.matrimony.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileSearchRequest {

    private Integer minAge;
    private Integer maxAge;

    private String religion;
    private String caste;

    private String city;
    private String state;
    private String country;

    private String gender;
}
