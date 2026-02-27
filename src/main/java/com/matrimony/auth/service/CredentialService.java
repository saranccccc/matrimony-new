package com.matrimony.auth.service;

import com.matrimony.auth.dto.LoginRequest;
import com.matrimony.auth.dto.LoginResponse;
import com.matrimony.auth.entity.User;
import com.matrimony.auth.entity.UserStatus;
import com.matrimony.auth.repository.UserRepository;
import com.matrimony.auth.security.JwtTokenProvider;
import com.matrimony.common.exception.CustomException;
import com.matrimony.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CredentialService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;

    public void setCredentials(String userId, String username, String password) {
        // todo: password encryption at the backend itself

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setPasswordHash(passwordEncoder.encode(password));
        userRepository.save(user);
        log.info("Password is encrypted and saved for user:{}", userId);
    }

    public LoginResponse login(LoginRequest request) {
        log.info("User login starts for user:{},", request.getUsername());
        User user = userRepository.findByProfileIdOrEmailOrMobileNo(request.getUsername(), request.getUsername(), request.getUsername())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (UserStatus.ACTIVE != user.getUserStatus()) {
            log.info("User login - user:{} is not active", request.getUsername());
            throw new CustomException(ErrorCode.USER_NOT_ACTIVE);
        }

        if (user.getIsBlocked()) {
            throw new CustomException(ErrorCode.USER_BLOCKED);
        }
        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPasswordHash())) {
            log.info("User login - user:{} invalid credentials", request.getUsername());
            throw new CustomException(ErrorCode.INVALID_CREDENTIALS);
        }

        return LoginResponse.builder()
                .accessToken(jwtTokenProvider.generateToken(user))
                .refreshToken(refreshTokenService.createRefreshToken(user.getUserId()).getToken())
                .build();
    }
}
