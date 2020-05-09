package com.bangstagram.user.property;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class NaverProperty {
    @Value("${oauth.naver.clientId}")
    private String naverClientId;

    @Value("${oauth.naver.clientSecret}")
    private String naverClientSecret;

    @Value("${oauth.naver.tokenRequestUrl}")
    private String naverTokenRequestUrl;

    @Value("${oauth.naver.profileRequestUrl}")
    private String naverProfileRequestUrl;
}
