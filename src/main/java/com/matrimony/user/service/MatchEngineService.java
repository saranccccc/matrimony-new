package com.matrimony.user.service;

import com.matrimony.common.exception.CustomException;
import com.matrimony.common.exception.ErrorCode;
import com.matrimony.user.dto.ProfileStatus;
import com.matrimony.user.dto.UserProfileResponse;
import com.matrimony.user.entity.PartnerPreference;
import com.matrimony.user.entity.UserProfile;
import com.matrimony.user.repository.PartnerPreferenceRepository;
import com.matrimony.user.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MatchEngineService {

    private final UserProfileRepository profileRepository;
    private final PartnerPreferenceRepository preferenceRepository;

    public List<UserProfileResponse> findMatches(String userId) {

        UserProfile user = profileRepository.findByUserId(userId).orElseThrow(() -> new CustomException(ErrorCode.PROFILE_NOT_FOUND));

        PartnerPreference preference = preferenceRepository.findByUserId(userId).orElseThrow(() -> new CustomException(ErrorCode.PREFERENCE_NOT_FOUND));

        List<UserProfile> candidates = profileRepository.findAll((root, query, cb) -> cb.and(cb.notEqual(root.get("userId"), userId), root.get("status").in(ProfileStatus.APPROVED, ProfileStatus.COMPLETED)));

        return candidates.stream().map(candidate -> {
            int score = MatchScoreCalculator.calculate(user, candidate, preference);
            return UserProfileResponse.builder().userId(candidate.getUserId()).firstName(candidate.getFirstName()).age(candidate.getAge()).religion(candidate.getReligion()).caste(candidate.getCaste()).city(candidate.getCity()).matchScore(score).build();
        }).sorted((a, b) -> b.getMatchScore() - a.getMatchScore()).limit(50).toList();
    }
}
