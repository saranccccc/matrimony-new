package com.matrimony.user.service;

import com.matrimony.user.dto.ProfileSearchRequest;
import com.matrimony.user.dto.UserProfileResponse;
import com.matrimony.user.entity.UserProfile;
import com.matrimony.user.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileSearchService {

    private final UserProfileRepository profileRepository;

    public Page<UserProfileResponse> search(String currentUserId, ProfileSearchRequest request, Pageable pageable) {
        Specification<UserProfile> spec = ProfileSpecification.search(request, currentUserId);
        return profileRepository.findAll(spec, pageable).map(this::mapToResponse);
    }

    private UserProfileResponse mapToResponse(UserProfile profile) {
        return UserProfileResponse.builder().userId(profile.getUserId()).firstName(profile.getFirstName()).age(profile.getAge()).gender(profile.getGender()).religion(profile.getReligion()).caste(profile.getCaste()).education(profile.getEducation()).profession(profile.getProfession()).city(profile.getCity()).state(profile.getState()).country(profile.getCountry()).status(profile.getStatus()).build();
    }
}
