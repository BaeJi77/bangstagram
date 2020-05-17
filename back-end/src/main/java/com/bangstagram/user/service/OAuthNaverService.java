package com.bangstagram.user.service;

import com.bangstagram.user.configure.security.JwtAuthenticationToken;
import com.bangstagram.user.controller.dto.request.AuthRequestDto;
import com.bangstagram.user.controller.dto.response.AuthResponseDto;
import com.bangstagram.user.domain.model.oauth.naver.NaverLoginApi;
import com.bangstagram.user.domain.model.oauth.naver.NaverProfileApi;
import com.bangstagram.user.domain.model.user.User;
import com.bangstagram.user.util.HttpUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
public class OAuthNaverService implements OAuthServiceImpl<NaverLoginApi.Tokens, NaverProfileApi.UserInfo> {
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
        //TODO 네이버 login Token 가져오기

        String loginApiUrl = naverLoginApi.getLoginApiUrl("authorization_code", code, state[0]);

        String loginApiResult = HttpUtils.getMethod(loginApiUrl, Collections.EMPTY_MAP, "application/x-www-form-urlencoded;charset=utf-8");

        NaverLoginApi.Tokens tokens = newAccessToken(loginApiResult);
        String header = tokens.parseToken2Header();

        //TODO 네이버 profile 정보 가져오기

        String profileApiUrl = naverProfileApi.getUrl();

        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("Authorization", header);

        String profileApiResult = HttpUtils.getMethod(profileApiUrl, requestHeaders, "application/json; charset=utf-8");

        NaverProfileApi.UserInfo userInfo = newUserInfo(profileApiResult);
        String email = userInfo.getEmail();

        // 로그인 인증(AuthenticationManager 거치지 않고, SecurityContextHolder에 Authentication 등록)
        AuthResponseDto authResponseDto = userService.authLogin(email);
        JwtAuthenticationToken authenticated
                = new JwtAuthenticationToken(authResponseDto.getUser().getId(), null, AuthorityUtils.createAuthorityList("ROLE_USER"));
        authenticated.setDetails(authResponseDto);
        SecurityContextHolder.getContext().setAuthentication(authenticated);

        return (AuthResponseDto) authenticated.getDetails();
    }

    @Override
    public NaverLoginApi.Tokens newAccessToken(String loginApiResult) {
        log.info("[naver login api result] loginApiResult: {}", loginApiResult);

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
    public NaverProfileApi.UserInfo newUserInfo(String profileApiResult) {
        log.info("[naver profile api info] profileApiResult: {}", profileApiResult);

        NaverProfileApi.UserInfo userInfo = new NaverProfileApi.UserInfo();
        try {
            JsonNode jsonNode = mapper.readTree(profileApiResult);
            userInfo = NaverProfileApi.UserInfo.builder()
                    .name(jsonNode.path("response").path("name").textValue())
                    .email(jsonNode.path("response").path("email").textValue())
                    .build();

        } catch (JsonProcessingException e) {
            log.error("error message: {}", e.getMessage());
        }

        return userInfo;
    }
}
