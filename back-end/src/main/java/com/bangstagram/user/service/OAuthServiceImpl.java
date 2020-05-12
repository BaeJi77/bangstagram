package com.bangstagram.user.service;

import com.bangstagram.user.controller.dto.response.AuthResponseDto;

public interface OAuthServiceImpl<T,R> {
    AuthResponseDto login(String code, String... state);

    T newAccessToken(String result);

    R newUserInfo(String result);
}
