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
    private String email;

    @NotEmpty
    private String password;

    JoinRequestDto() {
    }

    public JoinRequestDto(String name, String loginEmail, String loginPassword) {
        this.name = name;
        this.email = loginEmail;
        this.password = loginPassword;
    }

    public User newUser(PasswordEncoder passwordEncoder) {
        return new User(name, email, passwordEncoder.encode(password));
    }
}
