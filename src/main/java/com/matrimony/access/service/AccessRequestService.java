package com.matrimony.access.service;

import com.matrimony.access.dto.AccessRequestStatus;
import com.matrimony.access.dto.AccessRequestType;
import com.matrimony.access.entity.AccessRequest;
import com.matrimony.access.repository.AccessRequestRepository;
import com.matrimony.common.exception.CustomException;
import com.matrimony.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccessRequestService {

    private final AccessRequestRepository repository;

    @Transactional
    public void sendRequest(String requesterId, String ownerId, AccessRequestType type) {
        if (requesterId.equals(ownerId)) {
            throw new CustomException(ErrorCode.INVALID_OPERATION);
        }
        Optional<AccessRequest> existing = repository.findByRequesterUserIdAndOwnerUserIdAndType(requesterId, ownerId, type);

        if (existing.isPresent()) {
            throw new CustomException(ErrorCode.REQUEST_ALREADY_EXISTS);
        }
        AccessRequest request = AccessRequest.builder().requesterUserId(requesterId).ownerUserId(ownerId).type(type).status(AccessRequestStatus.PENDING).build();
        repository.save(request);
    }

    @Transactional
    public void respondRequest(Long requesterId, String ownerId, AccessRequestStatus status) {
        AccessRequest request = repository.findById(requesterId).orElseThrow(() -> new CustomException(ErrorCode.REQUEST_NOT_FOUND));
        if (!request.getOwnerUserId().equals(ownerId)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        if (request.getStatus() != AccessRequestStatus.PENDING) {
            throw new CustomException(ErrorCode.INVALID_OPERATION);
        }

        if (status == AccessRequestStatus.APPROVED || status == AccessRequestStatus.REJECTED) {
            request.setStatus(status);
        } else {
            throw new CustomException(ErrorCode.INVALID_OPERATION);
        }
        repository.save(request);
    }

    public boolean hasApprovedAccess(String requesterId, String ownerId, AccessRequestType type) {
        return repository.findByRequesterUserIdAndOwnerUserIdAndType(requesterId, ownerId, type).map(req -> req.getStatus() == AccessRequestStatus.APPROVED).orElse(false);
    }

    public Page<AccessRequest> getPendingRequests(String ownerId, Pageable pageable) {
        return getRequests(ownerId, AccessRequestStatus.PENDING, pageable);
    }

    public Page<AccessRequest> getRequests(String ownerId, AccessRequestStatus AccessRequestStatus, Pageable pageable) {
        return repository.findByOwnerUserIdAndStatus(ownerId, AccessRequestStatus.PENDING, pageable);
    }
}
