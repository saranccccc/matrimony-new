package com.matrimony.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PartnerPreferenceResponse {
    private Long id;
    private String userId;
    private Integer minAge;
    private Integer maxAge;
    private Boolean religionNoBar;
    private Boolean casteNoBar;
    private Set<String> preferredReligions = new HashSet<>();
    private Set<String> preferredCastes = new HashSet<>();
    private Set<String> cities = new HashSet<>();

}
