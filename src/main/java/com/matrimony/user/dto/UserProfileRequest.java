package com.matrimony.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserProfileRequest {

    private String firstName;
    private String lastName;
    private Integer age;
    private String gender;

    private String religion;
    private String caste;
    private String subCaste;

    private String education;
    private String profession;

    private String city;
    private String state;
    private String country;

    private String aboutMe;

    private VisibilityLevel photoVisibility;
    private VisibilityLevel contactVisibility;
}
