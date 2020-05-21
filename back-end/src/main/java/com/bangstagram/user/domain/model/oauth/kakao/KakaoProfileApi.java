package com.bangstagram.user.domain.model.oauth.kakao;

import lombok.Builder;
import lombok.Getter;

/***
 * author: Hyo-Jin Kim
 * Date: 2020.05.08
 */

@Getter
public class KakaoProfileApi {
    private final String url;

    public KakaoProfileApi(String url) {
        this.url = url;
    }

    @Getter
    static public class ProfileInfo {
        String name;
        String email;

        public ProfileInfo() {}

        @Builder
        public ProfileInfo(String name, String email) {
            this.name = name;
            this.email = email;
        }
    }
}
