package com.matrimony.auth.service;

import com.matrimony.auth.entity.User;
import com.matrimony.auth.entity.UserStatus;
import com.matrimony.auth.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CredentialServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    CredentialService credentialService;

    @Test
    void shouldLoginSuccessfully() {

        User user = new User();
        user.setUserName("999");
        user.setPasswordHash("hashed");
        user.setUserStatus(UserStatus.ACTIVE);

        when(userRepository.findByUserName("999"))
                .thenReturn(Optional.of(user));
        when(passwordEncoder.matches("pwd", "hashed"))
                .thenReturn(true);

        String token = credentialService.login(
                new com.matrimony.auth.dto.LoginRequest() {{
                    setUsername("999");
                    setPassword("pwd");
                }}
        );

        assertNotNull(token);
    }
}
