package com.bangstagram.user.service;

import com.bangstagram.user.domain.model.api.response.AuthResponseDto;
import com.bangstagram.user.domain.model.oauth.kakao.KakaoLoginApi;
import com.bangstagram.user.domain.model.oauth.kakao.KakaoProfileApi;
import com.bangstagram.user.domain.model.oauth.naver.NaverLoginApi;
import com.bangstagram.user.domain.model.oauth.naver.NaverProfileApi;
import com.bangstagram.user.util.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class OAuthService {
    private final UserService userService;

    private final NaverLoginApi naverLoginApi;

    private final NaverProfileApi naverProfileApi;

    private final KakaoLoginApi kakaoLoginApi;

    private final KakaoProfileApi kakaoProfileApi;

    public OAuthService(UserService userService,
                        NaverLoginApi naverLoginApi, NaverProfileApi naverProfileApi,
                        KakaoLoginApi kakaoLoginApi, KakaoProfileApi kakaoProfileApi) {
        this.userService = userService;
        this.naverLoginApi = naverLoginApi;
        this.naverProfileApi = naverProfileApi;
        this.kakaoLoginApi = kakaoLoginApi;
        this.kakaoProfileApi = kakaoProfileApi;
    }

    public AuthResponseDto loginWithNaver(String code, String state) {
        String loginApiUrl = naverLoginApi.getLoginApiUrl("authorization_code", code, state);

        String naverLoginApiResult = HttpUtils.getMethod(loginApiUrl, Collections.EMPTY_MAP, "application/x-www-form-urlencoded;charset=utf-8");
        log.info("naver login result: {}", naverLoginApiResult);

        //TODO: (String) naverLoginApiResult -> JSONObject로 변경, accessToken 가져오기
        NaverLoginApi.Tokens tokens = new NaverLoginApi.Tokens(naverLoginApiResult);
        String header = tokens.parseToken2Header();
        // ------------
        String profileApiUrl = naverProfileApi.getUrl();

        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("Authorization", header);

        String naverProfileApiResult = HttpUtils.getMethod(profileApiUrl, requestHeaders, "application/json; charset=utf-8");
        log.info("naver profile info: {}", naverProfileApiResult);

        //TODO: (String) naverProfileApiResult -> JSONObject로 변경, email가져오기
        NaverProfileApi.UserInfo userInfo = new NaverProfileApi.UserInfo(naverProfileApiResult);
        String name = userInfo.getName();
        String email = userInfo.getEmail();

        return userService.authLogin(email);
    }

    public AuthResponseDto loginWithKakao(String code) {
        String loginApiUrl = kakaoLoginApi.getLoginApiUrl(code);
        // String requestBody = kakaoLoginApi.makeRequestBody(code);

        // log.info("{}", requestBody);
        String kakaoLoginApiResult = HttpUtils.getMethod(loginApiUrl, Collections.EMPTY_MAP, "application/x-www-form-urlencoded;charset=utf-8");

        log.info("kakao login result: {}", kakaoLoginApiResult);

        //TODO: (String) naverLoginApiResult -> JSONObject로 변경, accessToken 가져오기
        KakaoLoginApi.Tokens tokens = new KakaoLoginApi.Tokens(kakaoLoginApiResult);
        String header = tokens.parseToken2Header();

        log.info("header: {}", header);
        // ---
        String profileApiUrl = kakaoProfileApi.getUrl();

        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("Authorization", header);

        String kakaoProfileApiResult = HttpUtils.getMethod(profileApiUrl, requestHeaders, "application/json; charset=utf-8");
        log.info("kakao profile info: {}", kakaoProfileApiResult);

        //TODO: (String) naverProfileApiResult -> JSONObject로 변경, email가져오기
        KakaoProfileApi.UserInfo userInfo = new KakaoProfileApi.UserInfo(kakaoProfileApiResult);
        String name = userInfo.getName();
        String email = userInfo.getEmail();

        return userService.authLogin(email);
    }
}
