package com.bangstagram.user.controller.dto.response;

import lombok.Getter;

import java.util.StringJoiner;

@Getter
public class CheckEmailResponseDto {
    private final boolean result;

    public CheckEmailResponseDto(boolean result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", CheckEmailResponseDto.class.getSimpleName() + "[", "]")
                .add("result=" + result)
                .toString();
    }
}
