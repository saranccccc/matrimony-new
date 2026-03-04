package com.matrimony.user.service;

import com.matrimony.common.exception.CustomException;
import com.matrimony.common.exception.ErrorCode;
import com.matrimony.user.dto.ProfileStatus;
import com.matrimony.user.dto.UserProfileRequest;
import com.matrimony.user.dto.UserProfileResponse;
import com.matrimony.user.entity.UserProfile;
import com.matrimony.user.repository.PartnerPreferenceRepository;
import com.matrimony.user.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserProfileService {
    private final UserProfileRepository profileRepository;

    @Transactional
    public void createOrUpdateProfile(String userId, UserProfileRequest request) {
        log.info("Retrieve profile by userId:{},", userId);
        UserProfile profile = profileRepository.findByUserId(userId).orElse(new UserProfile());

        profile.setUserId(userId);
        profile.setFirstName(request.getFirstName());
        profile.setLastName(request.getLastName());
        profile.setAge(request.getAge());
        profile.setGender(request.getGender());
        profile.setReligion(request.getReligion());
        profile.setCaste(request.getCaste());
        profile.setSubCaste(request.getSubCaste());
        profile.setEducation(request.getEducation());
        profile.setProfession(request.getProfession());
        profile.setCity(request.getCity());
        profile.setState(request.getState());
        profile.setCountry(request.getCountry());
        profile.setAboutMe(request.getAboutMe());
        profile.setPhotoVisibility(request.getPhotoVisibility());
        profile.setContactVisibility(request.getContactVisibility());
        boolean completed = isProfileComplete(profile);
        profile.setProfileCompleted(completed);
        if (completed && profile.getStatus() == null) {
            profile.setStatus(ProfileStatus.COMPLETED);
        }
        profileRepository.save(profile);
        log.info("Profile saved successfully for userId:{},", userId);
    }

    public UserProfileResponse getProfile(String userId) {
        UserProfile profile = profileRepository.findByUserId(userId).orElseThrow(() -> new CustomException(ErrorCode.PROFILE_NOT_FOUND));
        return mapToResponse(profile);
    }

    private boolean isProfileComplete(UserProfile profile) {
        log.info("Calculating profile completeness for user:{},", profile.getUserId());
        return profile.getFirstName() != null && profile.getGender() != null && profile.getAge() != null && profile.getReligion() != null && profile.getEducation() != null && profile.getCity() != null;
    }

    private UserProfileResponse mapToResponse(UserProfile profile) {
        return UserProfileResponse.builder().userId(profile.getUserId()).firstName(profile.getFirstName()).lastName(profile.getLastName()).age(profile.getAge()).gender(profile.getGender()).religion(profile.getReligion()).caste(profile.getCaste()).subCaste(profile.getSubCaste()).education(profile.getEducation()).profession(profile.getProfession()).city(profile.getCity()).state(profile.getState()).country(profile.getCountry()).aboutMe(profile.getAboutMe()).status(profile.getStatus()).profileCompleted(profile.getProfileCompleted()).build();
    }
}
