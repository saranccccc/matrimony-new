package com.matrimony.admin.repository;

import com.matrimony.admin.dto.ModerationStatus;
import com.matrimony.admin.entity.ModerationRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModerationRepository extends JpaRepository<ModerationRequest, Long> {
    Page<ModerationRequest> findByStatus(ModerationStatus status, Pageable pageable);
}
