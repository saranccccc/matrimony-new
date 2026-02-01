package com.matrimony.auth.controller;

import com.matrimony.auth.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;

import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(AuthController.class)

class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;


    private AuthService authService;

    @Test
    void shouldRegister() throws Exception {
        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType("application/json")
                        .content("{\"fullName\":\"Ram\",\"mobileNo\":\"999\"}"))
                .andExpect(status().isOk());
    }
}
