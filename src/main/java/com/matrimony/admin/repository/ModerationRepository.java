package com.matrimony.admin.repository;

import com.matrimony.admin.dto.ModerationAction;
import com.matrimony.admin.dto.ModerationStatus;
import com.matrimony.admin.entity.ModerationRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ModerationRepository extends JpaRepository<ModerationRequest, Long> {
    Page<ModerationRequest> findByStatus(ModerationStatus status, Pageable pageable);

    Optional<ModerationRequest> findByActionAndTargetUserIdAndStatus(ModerationAction action, String targetUserId, ModerationStatus status);

    Optional<ModerationRequest> findByActionAndTargetPhotoIdAndStatus(ModerationAction action, Long targetPhotoId, ModerationStatus status);
}
