package com.matrimony.auth.service;

import com.matrimony.auth.entity.RefreshToken;
import com.matrimony.auth.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository repository;
    private final PasswordEncoder passwordEncoder;

    private final long REFRESH_EXPIRY_DAYS = 7;

    @Transactional
    public RefreshToken createRefreshToken(String userId) {

        repository.deleteByUserId(userId); // rotate old tokens

        String rawToken = UUID.randomUUID().toString();

        RefreshToken token = RefreshToken.builder()
                .token(passwordEncoder.encode(rawToken))
                .userId(userId)
                .expiryDate(LocalDateTime.now().plusDays(REFRESH_EXPIRY_DAYS))
                .revoked(false)
                .createdAt(LocalDateTime.now())
                .build();

        repository.save(token);

        token.setToken(rawToken); // return raw token only once
        return token;
    }

    @Transactional
    public void revokeToken(String userId) {
        repository.deleteByUserId(userId);
    }
}
