package com.bangstagram.user.domain.model.api.request;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
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
