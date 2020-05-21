package com.bangstagram.user.configure.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

/***
 * author: Hyo-Jin Kim
 * Date: 2020.05.09
 */

@Setter
@Getter
@ConfigurationProperties(prefix = "oauth.kakao")
public class KakaoProperty {
    @Value("${oauth.kakao.clientId}")
    private String kakaoClientId;

    @Value("${oauth.kakao.tokenRequestUrl}")
    private String kakaoLoginTokenUrl;

    @Value("${oauth.kakao.profileRequestUrl}")
    private String kakaoProfileInfoUrl;
}
