package com.bangstagram.user.controller.dto.request;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import java.util.StringJoiner;

/***
 * author: Hyo-Jin Kim
 * Date: 2020.05.01
 */

@Getter
public class AuthRequestDto {
    @NotBlank
    private String principal;

    @NotBlank
    private String credentials;

    protected AuthRequestDto() { // protected
    }

    public AuthRequestDto(String principal, String credentials) {
        this.principal = principal;
        this.credentials = credentials;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", AuthRequestDto.class.getSimpleName() + "[", "]")
                .add("principal='" + principal + "'")
                .add("credentials='" + credentials + "'")
                .toString();
    }
}
