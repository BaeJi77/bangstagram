package com.bangstagram.user.domain.model.oauth.kakao;

import lombok.Builder;
import lombok.Getter;

@Getter
public class KakaoProfileApi {
    private final String url;

    public KakaoProfileApi(String url) {
        this.url = url;
    }

    @Getter
    static public class UserInfo {
        String name;
        String email;

        public UserInfo() {}

        @Builder
        public UserInfo(String name, String email) {
            this.name = name;
            this.email = email;
        }
    }
}
