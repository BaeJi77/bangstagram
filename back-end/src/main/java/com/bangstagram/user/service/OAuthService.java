package com.bangstagram.user.service;

import com.bangstagram.user.domain.model.api.response.AuthResponseDto;
import com.bangstagram.user.domain.model.oauth.naver.NaverLoginApi;
import com.bangstagram.user.domain.model.oauth.naver.NaverProfileApi;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.bangstagram.user.util.OAuthUtils.get;

@Service
@Slf4j
public class OAuthService {
    private final UserService userService;

    private final NaverLoginApi naverLoginApi;

    private final NaverProfileApi naverProfileApi;

    public OAuthService(UserService userService,
                        NaverLoginApi naverLoginApi, NaverProfileApi naverProfileApi) {
        this.userService = userService;
        this.naverLoginApi = naverLoginApi;
        this.naverProfileApi = naverProfileApi;
    }

    public AuthResponseDto loginWithNaver(String code, String state) throws ParseException {
        String loginApiUrl = naverLoginApi.loginApiUrl("authorization_code",code,state);

        String naverLoginApiResult = get(loginApiUrl, Collections.EMPTY_MAP, "POST");
        log.info("login result: {}", naverLoginApiResult);

        //TODO: (String) naverLoginApiResult -> JSONObject로 변경, accessToken 가져오기
        NaverLoginApi.Tokens tokens = new NaverLoginApi.Tokens(naverLoginApiResult);
        String header = tokens.parseToken2Header();

        // ------------
        String profileApiUrl = naverProfileApi.profileApiUrl();

        Map<String,String> requestHeaders = new HashMap<>();
        requestHeaders.put("Authorization", header);

        String naverProfileApiResult = get(profileApiUrl, requestHeaders, "GET");
        log.info("naver profile info: {}", naverProfileApiResult);

        //TODO: (String) naverProfileApiResult -> JSONObject로 변경, email가져오기
        NaverProfileApi.UserInfo userInfo = new NaverProfileApi.UserInfo(naverProfileApiResult);
        String name = (String) userInfo.getName();
        String email = (String) userInfo.getEmail();

        return userService.authLogin(email);
    }


}
