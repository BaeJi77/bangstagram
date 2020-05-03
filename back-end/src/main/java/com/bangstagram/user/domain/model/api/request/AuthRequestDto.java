package com.bangstagram.user.domain.model.api.request;

import lombok.Getter;

@Getter
public class AuthRequestDto {
    private String principal;

    private String credentials;

    AuthRequestDto() {}

    public AuthRequestDto(String principal, String credentials) {
        this.principal = principal;
        this.credentials = credentials;
    }
}
