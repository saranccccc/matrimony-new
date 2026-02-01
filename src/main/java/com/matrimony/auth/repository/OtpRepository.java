package com.matrimony.auth.repository;

import com.matrimony.auth.entity.OtpVerification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OtpRepository extends JpaRepository<OtpVerification, Long> {

    Optional<OtpVerification> findTopByUserIdAndChannelOrderByOtpIdDesc(
            String userId, String channel);
}
