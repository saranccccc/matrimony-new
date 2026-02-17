package com.matrimony.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import com.matrimony.auth.entity.*;

public interface OtpRepository extends JpaRepository<OtpEntity, Long> {

    Optional<OtpEntity> findTopByUserIdAndChannelOrderByIdDesc(
            String userId,
            OtpChannel channel
    );
}
