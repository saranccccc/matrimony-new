package com.matrimony.auth.service;

import com.matrimony.audit.service.AuditService;
import com.matrimony.common.repository.OtpRepository;
import com.matrimony.auth.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    OtpRepository otpRepository;

    @Mock
    AuditService auditService;

    @InjectMocks
    AuthService authService;

    @Test
    void shouldRegisterUser() {
        String userId = authService.register("Ram", "9999999999", null);
        assertNotNull(userId);
    }
}
