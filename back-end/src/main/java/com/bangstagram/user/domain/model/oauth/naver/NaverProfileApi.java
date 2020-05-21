package com.bangstagram.user.domain.model.oauth.naver;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
public class NaverProfileApi {
    private final String url;

    public NaverProfileApi(String url) {
        this.url = url;
    }

    /***
     *  네이버 회원 프로필 조회 API
     *  url    - https://openapi.naver.com/v1/nid/me
     *  header - access_token
     */

    @Getter
    @ToString
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
