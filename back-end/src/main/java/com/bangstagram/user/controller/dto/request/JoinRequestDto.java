package com.bangstagram.user.controller.dto.request;

import com.bangstagram.user.domain.model.user.User;
import lombok.Getter;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.StringJoiner;

/***
 * author: Hyo-Jin Kim
 * Date: 2020.05.01
 */

@Getter
public class JoinRequestDto {
    @NotBlank
    private String name;

    @NotBlank
    @Email
    private String email;

    @NotEmpty
    private String password;

    protected JoinRequestDto() { // protected
    }

    public JoinRequestDto(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public User newUser(PasswordEncoder passwordEncoder) {
        return new User(name, email, passwordEncoder.encode(password));
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", JoinRequestDto.class.getSimpleName() + "[", "]")
                .add("name='" + name + "'")
                .add("email='" + email + "'")
                .add("password='" + password + "'")
                .toString();
    }
}
