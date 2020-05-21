package com.bangstagram.user.controller.dto.request;

import com.bangstagram.user.domain.model.user.User;
import lombok.Getter;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Getter
public class JoinRequestDto {
    @NotBlank
    private String name;

    @NotBlank
    @Email
    private String loginEmail;

    @NotEmpty
    private String loginPassword;

    JoinRequestDto() {
    }

    public JoinRequestDto(String name, String loginEmail, String loginPassword) {
        this.name = name;
        this.loginEmail = loginEmail;
        this.loginPassword = loginPassword;
    }

    public User newUser(PasswordEncoder passwordEncoder, String oAuth) {
        return new User(name, loginEmail, passwordEncoder.encode(loginPassword), oAuth);
    }
}
