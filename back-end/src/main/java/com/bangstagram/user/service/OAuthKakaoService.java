package com.bangstagram.user.service;

import com.bangstagram.user.controller.dto.response.AuthResponseDto;
import com.bangstagram.user.domain.model.oauth.kakao.KakaoLoginApi;
import com.bangstagram.user.domain.model.oauth.kakao.KakaoProfileApi;
import com.bangstagram.user.util.HttpUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class OAuthKakaoService implements OAuthServiceImpl<KakaoLoginApi.Tokens,KakaoProfileApi.UserInfo> {
    private final UserService userService;

    private final KakaoLoginApi kakaoLoginApi;

    private final KakaoProfileApi kakaoProfileApi;

    private final ObjectMapper mapper;

    public OAuthKakaoService(UserService userService, ObjectMapper mapper,
                        KakaoLoginApi kakaoLoginApi, KakaoProfileApi kakaoProfileApi) {
        this.userService = userService;
        this.kakaoLoginApi = kakaoLoginApi;
        this.kakaoProfileApi = kakaoProfileApi;
        this.mapper = mapper;
    }

    @Override
    public AuthResponseDto login(String code, String... state) {
        //TODO 카카오 login Token 가져오기

        String loginApiUrl = kakaoLoginApi.getLoginApiUrl(code);
        // String requestBody = kakaoLoginApi.makeRequestBody(code);
        // log.info("{}", requestBody);

        String loginApiResult = HttpUtils.getMethod(loginApiUrl, Collections.EMPTY_MAP, "application/x-www-form-urlencoded;charset=utf-8");

        KakaoLoginApi.Tokens tokens = newAccessToken(loginApiResult);
        String header = tokens.parseToken2Header();

        //TODO 카카오 profile 정보 가져오기

        String profileApiUrl = kakaoProfileApi.getUrl();

        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("Authorization", header);

        String profileApiResult = HttpUtils.getMethod(profileApiUrl, requestHeaders, "application/json; charset=utf-8");

        KakaoProfileApi.UserInfo userInfo = newUserInfo(profileApiResult);
        String name = userInfo.getName();
        String email = userInfo.getEmail();

        return userService.authLogin(email);
    }

    @Override
    public KakaoLoginApi.Tokens newAccessToken(String loginApiResult) {
        log.info("kakao login api result: {}", loginApiResult);

        KakaoLoginApi.Tokens tokens = new KakaoLoginApi.Tokens();
        try {
            JsonNode jsonNode = mapper.readTree(loginApiResult);
            tokens = KakaoLoginApi.Tokens.builder()
                    .accessToken(jsonNode.path("access_token").textValue())
                    .refreshToken(jsonNode.path("refresh_token").textValue())
                    .tokenType(jsonNode.path("token_type").textValue())
                    .expiresIn(jsonNode.path("expires_in").asLong())
                    .scope(jsonNode.path("scope").textValue())
                    .build();

        } catch (JsonProcessingException e) {
            log.error("error message: {}", e.getMessage());
        }

        return tokens;
    }

    @Override
    public KakaoProfileApi.UserInfo newUserInfo(String profileApiResult) {
        log.info("kakao profile info: {}", profileApiResult);

        KakaoProfileApi.UserInfo userInfo = new KakaoProfileApi.UserInfo();
        try {
            JsonNode jsonNode = mapper.readTree(profileApiResult);
            userInfo = KakaoProfileApi.UserInfo.builder()
                    .name(jsonNode.path("kakao_account").path("profile").path("nickname").textValue())
                    .email(jsonNode.path("kakao_account").path("email").textValue())
                    .build();

        } catch (JsonProcessingException e) {
            log.error("error message: {}", e.getMessage());
        }

        return userInfo;
    }
}
