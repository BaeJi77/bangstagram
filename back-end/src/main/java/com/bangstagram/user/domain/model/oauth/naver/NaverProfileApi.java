package com.bangstagram.user.domain.model.oauth.naver;

import lombok.Getter;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

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
            JSONObject response = (JSONObject) jsonObject.get("response");

            this.name = (String) response.get("name");
            this.email = (String) response.get("email");
        }
    }
}
