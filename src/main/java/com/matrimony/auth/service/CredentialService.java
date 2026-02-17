package com.matrimony.auth.service;

import com.matrimony.auth.dto.LoginRequest;
import com.matrimony.auth.entity.User;
import com.matrimony.auth.entity.UserStatus;
import com.matrimony.auth.repository.UserRepository;
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


    public void setCredentials(String userId, String username, String password) {
    // todo: password encryption at the backend itself

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));


        user.setUserName(username);
        user.setPasswordHash(passwordEncoder.encode(password));


        userRepository.save(user);
        log.info("Password is encrypted and saved for user:{}",userId);
    }

    public String login(LoginRequest request) {
    log.info("User login starts for user:{},",request.getUsername());
        User user = userRepository.findByUserName(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (UserStatus.ACTIVE != user.getUserStatus()) {
            log.info("User login - user:{} is not active",request.getUsername());
            throw new RuntimeException("User not active");
        }

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPasswordHash())) {
            log.info("User login - user:{} invalid credentials",request.getUsername());
            throw new RuntimeException("Invalid credentials");
        }
return "";
    }
}
