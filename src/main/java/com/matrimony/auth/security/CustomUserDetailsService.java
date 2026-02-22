package com.matrimony.auth.security;

import com.matrimony.auth.entity.User;
import com.matrimony.auth.repository.UserRepository;
import com.matrimony.common.exception.CustomException;
import com.matrimony.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userId)
            throws UsernameNotFoundException {

        log.info("Loading user from DB: {}", userId);
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        return new CustomUserDetails(user.getUserId(), user.getPasswordHash()
        );
    }
}