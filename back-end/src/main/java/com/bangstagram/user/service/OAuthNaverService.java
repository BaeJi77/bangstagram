package com.bangstagram.user.service;

import com.bangstagram.user.configure.security.JwtAuthenticationToken;
import com.bangstagram.user.controller.dto.response.AuthResponseDto;
import com.bangstagram.user.domain.model.oauth.naver.NaverLoginApi;
import com.bangstagram.user.domain.model.oauth.naver.NaverProfileApi;
import com.bangstagram.user.util.HttpUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/***
 * author: Hyo-Jin Kim
 * Date: 2020.05.13
 */

@Service
@Slf4j
public class OAuthNaverService implements OAuthServiceImpl<NaverLoginApi.Tokens, NaverProfileApi.ProfileInfo> {
    private final UserService userService;

    private final ObjectMapper mapper;

    private final NaverLoginApi naverLoginApi;

    private final NaverProfileApi naverProfileApi;

    public OAuthNaverService(UserService userService, ObjectMapper mapper,
                             NaverLoginApi naverLoginApi, NaverProfileApi naverProfileApi) {
        this.userService = userService;
        this.mapper = mapper;
        this.naverLoginApi = naverLoginApi;
        this.naverProfileApi = naverProfileApi;
    }

    @Override
    public AuthResponseDto login(String code, String... state) {
        // 1. 사용자 토큰 발급 요청 API 호출하여 access_token 가져오기
        String accessTokenHeader = getAccessTokenAsHeader(code, state[0]);

        // 2. 사용자 정보 요청 API 호출하여 회원 정보 가져오기
        NaverProfileApi.ProfileInfo profileInfo = getProfileInfo(accessTokenHeader);
        String email = profileInfo.getEmail();

        // 3. 로그인 인증(AuthenticationManager 거치지 않고, SecurityContextHolder에 Authentication 등록)
        AuthResponseDto authResponseDto = userService.authLogin(email);
        JwtAuthenticationToken authenticated
                = new JwtAuthenticationToken(authResponseDto.getUser().getId(), null, AuthorityUtils.createAuthorityList("ROLE_USER"));
        authenticated.setDetails(authResponseDto);
        SecurityContextHolder.getContext().setAuthentication(authenticated);

        return (AuthResponseDto) authenticated.getDetails();
    }

    private String getAccessTokenAsHeader(String code, String state) {
        String loginApiUrl = naverLoginApi.getLoginApiUrl("authorization_code", code, state);

        String loginApiResult = HttpUtils.getMethod(loginApiUrl, Collections.EMPTY_MAP, "application/x-www-form-urlencoded;charset=utf-8");

        NaverLoginApi.Tokens tokens = newAccessToken(loginApiResult);

        return tokens.parseToken2Header(); // Bearer ~
    }

    private NaverProfileApi.ProfileInfo getProfileInfo(String accessTokenHeader) {
        String profileApiUrl = naverProfileApi.getUrl();

        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("Authorization", accessTokenHeader);

        String profileApiResult = HttpUtils.getMethod(profileApiUrl, requestHeaders, "application/json; charset=utf-8");

        return newProfileInfo(profileApiResult);
    }

    @Override
    public NaverLoginApi.Tokens newAccessToken(String loginApiResult) {
        log.info("[naver request access_token api result] loginApiResult: {}", loginApiResult);

        NaverLoginApi.Tokens tokens = new NaverLoginApi.Tokens();
        try {
            JsonNode jsonNode = mapper.readTree(loginApiResult);
            tokens = NaverLoginApi.Tokens.builder()
                    .accessToken(jsonNode.path("access_token").textValue())
                    .refreshToken(jsonNode.path("refresh_token").textValue())
                    .tokenType(jsonNode.path("token_type").textValue())
                    .expiresIn(jsonNode.path("expires_in").textValue())
                    .build();

        } catch (JsonProcessingException e) {
            log.error("error message: {}", e.getMessage());
        }

        return tokens;
    }

    @Override
    public NaverProfileApi.ProfileInfo newProfileInfo(String profileApiResult) {
        log.info("[naver ProfileInfo api] profileApiResult: {}", profileApiResult);

        NaverProfileApi.ProfileInfo profileInfo = new NaverProfileApi.ProfileInfo();
        try {
            JsonNode jsonNode = mapper.readTree(profileApiResult);
            profileInfo = NaverProfileApi.ProfileInfo.builder()
                    .name(jsonNode.path("response").path("name").textValue())
                    .email(jsonNode.path("response").path("email").textValue())
                    .build();

        } catch (JsonProcessingException e) {
            log.error("error message: {}", e.getMessage());
        }

        return profileInfo;
    }
}
