package com.matrimony.auth.controller;

import com.matrimony.auth.dto.RegisterRequest;
import com.matrimony.auth.service.AuthService;
import com.matrimony.auth.service.OtpService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.BDDMockito.given;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.util.ResourceUtils;

import java.nio.file.Files;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private OtpService otpService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldRegister() throws Exception {
        given(authService.register(any(),any(),any())).willReturn("UserID");
        willDoNothing().given(otpService).generateOtp(any(), any());

        // Load JSON file from src/test/resources
        String path = ResourceUtils.getFile("classpath:json/register-request.json").getAbsolutePath();
        String json = Files.readString(java.nio.file.Paths.get(path));

        // Convert JSON to DTO (optional, type-safe)
        RegisterRequest request = objectMapper.readValue(json, RegisterRequest.class);

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }
}
