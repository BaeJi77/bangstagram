package com.bangstagram.user.service;

import com.bangstagram.user.domain.model.api.response.AuthResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.bangstagram.user.util.OAuthUtils.get;

@Service
@Slf4j
public class OAuthService {
    @Value("${oauth.naver.clientId}")
    private String CLIENT_ID;

    @Value("${oauth.naver.clientSecret}")
    private String CLIENT_SECRET;

    @Value("${oauth.naver.loginTokenApi}")
    private String NAVER_LOGIN_TOKEN_API;

    @Value("${oauth.naver.memberProfileApi}")
    private String NAVER_MEMBER_PROFILE_API;

    private final UserService userService;

    public OAuthService(UserService userService) {
        this.userService = userService;
    }

    public AuthResponseDto loginWithNaver(String code, String state) throws ParseException {
        log.debug("client_id: {}, client_secret: {}, naver_login_token_api: {}, naver_member_profile_api: {}",
                CLIENT_ID, CLIENT_SECRET, NAVER_LOGIN_TOKEN_API, NAVER_MEMBER_PROFILE_API);

        /*** 네이버 아이디로 로그인 API
         * url   - nid.naver.com/oauth2.0/token
         * param - grant_type(발급:'authorization_code', 갱신:'REFRESH_TOKEN', 삭제: 'delete')
         *       - client_id(네이버 API 아이디)
         *       - client_secret(네이버 API 시크릿 키)
         *       - code(로그인 인증 요청 API 호출 성공 후 받은 인증코드 값)
         *       - state(사이트 간 요청 위조 공격 방지를 위해 애플리케이션에서 생성한 상태 토큰값)
         */
        String grantType = "authorization_code";

        StringBuilder loginApiUrl = new StringBuilder();
        loginApiUrl.append(NAVER_LOGIN_TOKEN_API)
                .append("?grant_type="+grantType)
                .append("&client_id="+CLIENT_ID)
                .append("&client_secret="+CLIENT_SECRET)
                .append("&code="+code)
                .append("&state="+state);

        String naverLoginApiResult = get(loginApiUrl.toString(), Collections.EMPTY_MAP, "POST");
        log.info("loginResult: {}", naverLoginApiResult);

        //TODO: (String) naverLoginApiResult -> JSONObject로 변경, accessToken 가져오기
        JSONObject jsonLoginResult = toJson(naverLoginApiResult);
        String accessToken = (String) jsonLoginResult.get("access_token");
        String header = "Bearer " + accessToken;

        /***
         *  네이버 회원 프로필 조회 API
         *  url    - https://openapi.naver.com/v1/nid/me
         *  header - access_token
         */
        Map<String,String> requestHeaders = new HashMap<>();
        requestHeaders.put("Authorization", header);

        String naverProfileApiResult = get(NAVER_MEMBER_PROFILE_API, requestHeaders, "GET");
        log.info("naver profile info: {}", naverProfileApiResult);

        //TODO: (String) naverProfileApiResult -> JSONObject로 변경, email가져오기
        JSONObject jsonProfileResult = toJson(naverProfileApiResult);
        JSONObject response = (JSONObject) jsonProfileResult.get("response");
        String name = (String) response.get("name");
        String email = (String) response.get("email");

        return userService.authLogin(email);
    }

    private static JSONObject toJson(String jsonStr) throws ParseException {
        JSONParser jsonParser = new JSONParser();
        return (JSONObject) jsonParser.parse(jsonStr);
    }
}
