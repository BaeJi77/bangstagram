package com.bangstagram.user.controller.dto.request;

import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

/***
 * author: Hyo-Jin Kim
 * Date: 2020.05.01
 */

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
