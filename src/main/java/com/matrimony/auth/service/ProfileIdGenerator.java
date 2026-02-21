package com.matrimony.auth.service;

import com.matrimony.auth.entity.ProfileIdSequenceEntity;
import com.matrimony.auth.repository.ProfileIdSequenceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProfileIdGenerator {

    private final ProfileIdSequenceRepository repository;

    public ProfileIdGenerator(ProfileIdSequenceRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public String generateProfileId() {
        ProfileIdSequenceEntity profileIdSequenceEntity = new ProfileIdSequenceEntity();
        profileIdSequenceEntity = repository.save(profileIdSequenceEntity);
        return "SM" + profileIdSequenceEntity.getId();
    }
}
