package com.bangstagram.user.domain.model.oauth.naver;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;

@Getter
@Slf4j
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
    static public class UserInfo {
        String name;
        String email;

        public UserInfo(String name, String email) {
            this.name = name;
            this.email = email;
        }

        public UserInfo(String profileApiResult) {
            try {
                JSONObject jsonObject = new JSONObject(profileApiResult);
                this.name = jsonObject.getJSONObject("response").getString("name");
                this.email = jsonObject.getJSONObject("response").getString("email");
            } catch (JSONException jsonException) {
                log.error("error message: {}", jsonException.getMessage());
            }
        }
    }
}
