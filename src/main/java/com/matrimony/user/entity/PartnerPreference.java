package com.matrimony.user.entity;

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

@Entity
@Table(name = "partner_preferences")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PartnerPreference extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String userId;

    private Integer minAge;
    private Integer maxAge;

    private Boolean religionNoBar;
    private Boolean casteNoBar;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "partner_preferences_religions", joinColumns = @JoinColumn(name = "preference_id"))
    @Column(name = "religion", nullable = false)
    private Set<String> preferredReligions = new HashSet<>();

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "partner_preferences_castes", joinColumns = @JoinColumn(name = "preference_id"))
    @Column(name = "caste", nullable = false)
    private Set<String> preferredCastes = new HashSet<>();

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "partner_preferences_cities", joinColumns = @JoinColumn(name = "preference_id"))
    @Column(name = "city", nullable = false)
    private Set<String> cities = new HashSet<>();

}
