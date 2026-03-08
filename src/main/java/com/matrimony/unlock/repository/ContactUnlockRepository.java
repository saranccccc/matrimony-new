package com.matrimony.unlock.repository;

import com.matrimony.unlock.entity.ContactUnlock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ContactUnlockRepository extends JpaRepository<ContactUnlock, Long> {

    Optional<ContactUnlock> findByRequesterUserIdAndTargetUserId(String requesterUserId, String targetUserId);

    boolean existsByRequesterUserIdAndTargetUserId(String requesterUserId, String targetUserId);
}
