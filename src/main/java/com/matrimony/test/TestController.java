package com.matrimony.test;

import com.matrimony.auth.repository.UserRepository;
import com.matrimony.common.repository.OtpRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/test")
public class TestController {

    private final UserRepository userRepository;
    private final OtpRepository otpRepository;

    @GetMapping("/deleteAll")
    public ResponseEntity<?> register() {
        otpRepository.deleteAll();
        userRepository.deleteAll();
        return ResponseEntity.ok("Deleted All user");
    }

}
