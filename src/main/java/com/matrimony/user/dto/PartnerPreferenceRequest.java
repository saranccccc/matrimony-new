package com.matrimony.user.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class PartnerPreferenceRequest {
    private Integer minAge;
    private Integer maxAge;
    private Set<String> preferredCities;
    private Set<String> preferredReligions;
    private Set<String> preferredCastes;
}
