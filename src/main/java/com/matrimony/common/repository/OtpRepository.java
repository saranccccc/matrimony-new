package com.matrimony.common.repository;

import com.matrimony.common.entity.OtpChannel;
import com.matrimony.common.entity.OtpEntity;
import com.matrimony.common.entity.OtpStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface OtpRepository extends JpaRepository<OtpEntity, Long> {

    Optional<OtpEntity> findTopByUserIdAndChannelOrderByIdDesc(String userId, OtpChannel channel);

    Optional<OtpEntity> findTopByUserIdAndChannelAndDestination(String userId, OtpChannel channel, String destination);

    Optional<OtpEntity> findTopByUserIdAndChannelAndStatusOrderByCreatedAtDesc(String userId, OtpChannel channel, OtpStatus status);

    Optional<OtpEntity> findTopByUserIdAndChannelOrderByCreatedAtDesc(String userId, OtpChannel channel);

    @Modifying
    @Query("UPDATE OtpEntity o         SET o.status = 'EXPIRED'        WHERE o.userId = :userId        AND o.channel = :channel        AND o.status = 'ACTIVE'    ")
    void expirePreviousActiveOtps(String userId, OtpChannel channel);

    boolean existsByUserIdAndStatus(String userId, OtpStatus status);
}
