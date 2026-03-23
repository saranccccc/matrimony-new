package com.matrimony.user.repository;

import com.matrimony.user.dto.ProfileStatus;
import com.matrimony.user.entity.UserProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long>, JpaSpecificationExecutor<UserProfile> {

    Optional<UserProfile> findByUserId(String userId);

    boolean existsByUserId(String userId);

    Page<UserProfile> findByStatus(ProfileStatus status, Pageable pageable);

    long countByUserIdAndStatus(String userId, ProfileStatus status);

}
