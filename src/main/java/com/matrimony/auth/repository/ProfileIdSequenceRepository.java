package com.matrimony.auth.repository;

import com.matrimony.auth.entity.ProfileIdSequenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileIdSequenceRepository extends JpaRepository<ProfileIdSequenceEntity, Long> {

}
