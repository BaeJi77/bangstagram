package com.bangstagram.user.property;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class KakaoProperty {
    @Value("${oauth.kakao.clientId}")
    private String kakaoClientId;

    @Value("${oauth.kakao.tokenRequestUrl}")
    private String kakaoLoginTokenUrl;

    @Value("${oauth.kakao.profileRequestUrl}")
    private String kakaoProfileInfoUrl;
}
