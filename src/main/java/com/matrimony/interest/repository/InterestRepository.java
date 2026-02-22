package com.matrimony.interest.repository;

import com.matrimony.interest.dto.InterestStatus;
import com.matrimony.interest.entity.Interest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InterestRepository extends JpaRepository<Interest, Long> {

    Optional<Interest> findBySenderUserIdAndReceiverUserId(String senderUserId, String receiverUserId);

    Page<Interest> findByReceiverUserIdAndStatus(String receiverUserId, InterestStatus status, Pageable pageable);

    Page<Interest> findBySenderUserId(String senderUserId, Pageable pageable);
}
