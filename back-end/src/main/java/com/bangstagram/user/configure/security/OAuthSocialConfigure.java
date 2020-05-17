package com.bangstagram.user.configure.security;

import com.bangstagram.user.domain.model.oauth.kakao.KakaoLoginApi;
import com.bangstagram.user.domain.model.oauth.kakao.KakaoProfileApi;
import com.bangstagram.user.domain.model.oauth.naver.NaverLoginApi;
import com.bangstagram.user.domain.model.oauth.naver.NaverProfileApi;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/***
 * author: Hyo-Jin Kim
 * Date: 2020.05.16
 */

@Configuration
@EnableConfigurationProperties({NaverProperty.class, KakaoProperty.class})
public class OAuthSocialConfigure {
    private final NaverProperty naverProperty;

    private final KakaoProperty kakaoProperty;

    public OAuthSocialConfigure(NaverProperty naverProperty, KakaoProperty kakaoProperty) {
        this.naverProperty = naverProperty;
        this.kakaoProperty = kakaoProperty;
    }

    @Bean
    public NaverLoginApi naverLoginApi() {
        return new NaverLoginApi(naverProperty.getNaverClientId(), naverProperty.getNaverClientSecret(), naverProperty.getNaverTokenRequestUrl());
    }

    @Bean
    public NaverProfileApi naverProfileApi() {
        return new NaverProfileApi(naverProperty.getNaverProfileRequestUrl());
    }

    @Bean
    public KakaoLoginApi kakaoLoginApi() {
        return new KakaoLoginApi(kakaoProperty.getKakaoClientId(), kakaoProperty.getKakaoLoginTokenUrl());
    }

    @Bean
    public KakaoProfileApi kakaoProfileApi() {
        return new KakaoProfileApi(kakaoProperty.getKakaoProfileInfoUrl());
    }
}
