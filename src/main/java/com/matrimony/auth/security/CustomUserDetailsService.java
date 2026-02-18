package com.matrimony.auth.security;

import com.matrimony.auth.entity.User;
import com.matrimony.auth.repository.UserRepository;
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
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        log.info("Loading user from DB: {}", username);

        User user = userRepository.findByUserName(username).orElseThrow(() -> new UsernameNotFoundException("USER_NOT_FOUND"));

        return new CustomUserDetails(user.getUserName(), user.getPasswordHash()
        );
    }
}