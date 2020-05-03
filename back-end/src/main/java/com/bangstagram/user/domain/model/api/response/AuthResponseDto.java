package com.bangstagram.user.domain.model.api.response;

import com.bangstagram.user.domain.model.user.User;
import lombok.Getter;

@Getter
public class AuthResponseDto {
    private final User user;

    private final String jwtToken;

    public AuthResponseDto(User user, String jwtToken) {
        this.user = user;
        this.jwtToken = jwtToken;
    }
}
