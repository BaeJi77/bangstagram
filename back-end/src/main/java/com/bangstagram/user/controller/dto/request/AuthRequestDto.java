package com.bangstagram.user.controller.dto.request;

import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Getter
@ToString
public class AuthRequestDto {
    @NotBlank
    private String principal;

    @NotBlank
    private String credentials;

    AuthRequestDto() {
    }

    public AuthRequestDto(String principal, String credentials) {
        this.principal = principal;
        this.credentials = credentials;
    }
}
