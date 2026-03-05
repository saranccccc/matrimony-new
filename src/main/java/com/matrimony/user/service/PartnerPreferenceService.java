package com.matrimony.user.service;

import com.matrimony.common.dto.Result;
import com.matrimony.common.exception.CustomException;
import com.matrimony.common.exception.ErrorCode;
import com.matrimony.user.dto.PartnerPreferenceRequest;
import com.matrimony.user.dto.PartnerPreferenceResponse;
import com.matrimony.user.entity.PartnerPreference;
import com.matrimony.user.mapper.PartnerPreferenceMapper;
import com.matrimony.user.repository.PartnerPreferenceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class PartnerPreferenceService {
    private final PartnerPreferenceRepository preferenceRepository;
    private final PartnerPreferenceMapper partnerPreferenceMapper;

    @Transactional
    public Result createOrUpdatePartnerPreference(String userId, PartnerPreferenceRequest request) {
        log.info("Retrieve PartnerPreference by userId:{},", userId);
        PartnerPreference partnerPreference = preferenceRepository.findByUserId(userId).orElse(new PartnerPreference());
        partnerPreference.setUserId(userId);
        partnerPreference.setMaxAge(request.getMaxAge());
        partnerPreference.setMinAge(request.getMinAge());
        partnerPreference.setCities(request.getPreferredCities());
        partnerPreference.setPreferredReligions(request.getPreferredReligions());
        partnerPreference.setPreferredCastes(request.getPreferredCastes());
        preferenceRepository.save(partnerPreference);
        log.info("PartnerPreference saved successfully for userId:{},", userId);
        return Result.builder().status(0).build();

    }


    @Transactional(readOnly = true)
    public PartnerPreferenceResponse getPartnerPreference(String userId) {
        PartnerPreference partnerPreference = preferenceRepository.findByUserId(userId).orElseThrow(() -> new CustomException(ErrorCode.PROFILE_NOT_FOUND));
        return partnerPreferenceMapper.toResponse(partnerPreference);
    }

    @Transactional
    public void updateReligions(String userId, Set<String> religions) {
        PartnerPreference pref = preferenceRepository.findByUserId(userId).orElseThrow(() -> new RuntimeException("PREFERENCE_NOT_FOUND"));
        pref.getPreferredReligions().clear();
        if (religions != null) {
            pref.getPreferredReligions().addAll(normalize(religions));
        }
        preferenceRepository.save(pref);
    }

    private Set<String> normalize(Set<String> values) {
        Set<String> out = new HashSet<>();
        for (String v : values) {
            if (v != null) {
                String s = v.trim();
                if (!s.isEmpty()) out.add(s);
            }
        }
        return out;
    }
}
