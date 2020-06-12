package com.bangstagram.user.controller.dto.request;

import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.StringJoiner;

@Getter
public class CheckEmailRequestDto {
    @NotBlank
    @Email
    private String email;

    protected CheckEmailRequestDto() {
    }

    public CheckEmailRequestDto(@NotBlank @Email String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", CheckEmailRequestDto.class.getSimpleName() + "[", "]")
                .add("email='" + email + "'")
                .toString();
    }
}
