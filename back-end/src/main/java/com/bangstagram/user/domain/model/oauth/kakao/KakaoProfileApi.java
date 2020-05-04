package com.bangstagram.user.domain.model.oauth.kakao;

import lombok.Getter;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class KakaoProfileApi {
    private final String url;

    public KakaoProfileApi(String url) {
        this.url = url;
    }

    public String profileApiUrl() {
        return url;
    }

    @Getter
    static public class UserInfo {
        String name;
        String email;

        public UserInfo(String name, String email) {
            this.name = name;
            this.email = email;
        }

        public UserInfo(String profileApiResult) throws ParseException {
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(profileApiResult);
            JSONObject kakaoAccount = (JSONObject) jsonObject.get("kakao_account");
            JSONObject profile = (JSONObject) kakaoAccount.get("profile");

            this.name = (String) profile.get("nickname");
            this.email = (String) kakaoAccount.get("email");
        }
    }
}
