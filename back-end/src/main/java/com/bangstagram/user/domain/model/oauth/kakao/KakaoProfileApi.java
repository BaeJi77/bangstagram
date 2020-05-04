package com.bangstagram.user.domain.model.oauth.kakao;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;

@Slf4j
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

        public UserInfo(String name, String email) {
            this.name = name;
            this.email = email;
        }

        public UserInfo(String profileApiResult) {
            try {
                org.json.JSONObject jsonObject = new JSONObject(profileApiResult);
                this.name = jsonObject.getJSONObject("kakao_account").getJSONObject("profile").getString("nickname");
                this.email = jsonObject.getJSONObject("kakao_account").getString("email");
            } catch (JSONException jsonException) {
                log.error("error message: {}", jsonException.getMessage());
            }
        }
    }
}
