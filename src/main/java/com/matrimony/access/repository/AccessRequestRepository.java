package com.matrimony.access.repository;

import com.matrimony.access.dto.AccessRequestStatus;
import com.matrimony.access.dto.AccessRequestType;
import com.matrimony.access.entity.AccessRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccessRequestRepository extends JpaRepository<AccessRequest, Long> {

    Optional<AccessRequest> findByRequesterUserIdAndOwnerUserIdAndType(String requesterUserId, String ownerUserId, AccessRequestType type);

    Page<AccessRequest> findByOwnerUserIdAndStatus(String ownerUserId, AccessRequestStatus status, Pageable pageable);
}
