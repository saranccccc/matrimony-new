package com.matrimony.user.dto;

import com.matrimony.common.entity.BaseEntity;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
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
public class PartnerPreferenceResponse  {

    private Long id;
    private String userId;
    private Integer minAge;
    private Integer maxAge;
    private Boolean religionNoBar;
    private Boolean casteNoBar;
    private Set<String> preferredReligions = new HashSet<>();
    private Set<String> preferredCastes= new HashSet<>();
    private Set<String> cities = new HashSet<>();

}
