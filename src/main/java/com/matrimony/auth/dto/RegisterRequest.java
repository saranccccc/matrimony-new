package com.matrimony.auth.dto;

import com.matrimony.common.validator.StrongPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank(message = "Full name must not be blank")
    private String fullName;

    @NotBlank(message = "Mobile number must not be blank")
    private String mobileNo;

    @Email(message = "Invalid email format")
    private String email;

    @StrongPassword
    private String password;

    // todo add confirm password
}
