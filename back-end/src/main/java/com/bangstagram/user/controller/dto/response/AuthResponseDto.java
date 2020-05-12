package com.bangstagram.user.controller.dto.response;

import com.bangstagram.user.domain.model.user.User;
import lombok.Getter;

/***
 * author: Hyo-Jin Kim
 * Date: 2020.05.01
 */

@Getter
public class AuthResponseDto {
    private final User user;

    private final String jwtToken;

    public AuthResponseDto(User user, String jwtToken) {
        this.user = user;
        this.jwtToken = jwtToken;
    }
}
