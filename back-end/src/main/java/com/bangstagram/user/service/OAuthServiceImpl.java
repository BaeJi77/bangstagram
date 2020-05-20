package com.bangstagram.user.service;

import com.bangstagram.user.controller.dto.response.AuthResponseDto;

/***
 * author: Hyo-Jin Kim
 * Date: 2020.05.10
 */

public interface OAuthServiceImpl<T,R> {
    AuthResponseDto login(String code, String... state);

    T newAccessToken(String result);

    R newProfileInfo(String result);
}
