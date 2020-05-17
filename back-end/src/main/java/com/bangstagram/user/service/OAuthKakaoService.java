package com.bangstagram.user.service;

import com.bangstagram.user.configure.security.JwtAuthenticationToken;
import com.bangstagram.user.controller.dto.response.AuthResponseDto;
import com.bangstagram.user.domain.model.oauth.kakao.KakaoLoginApi;
import com.bangstagram.user.domain.model.oauth.kakao.KakaoProfileApi;
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
 * Date: 2020.05.08
 */

@Service
@Slf4j
public class OAuthKakaoService implements OAuthServiceImpl<KakaoLoginApi.Tokens, KakaoProfileApi.UserInfo> {
    private final UserService userService;

    private final ObjectMapper mapper;

    private final KakaoLoginApi kakaoLoginApi;

    private final KakaoProfileApi kakaoProfileApi;

    public OAuthKakaoService(UserService userService, ObjectMapper mapper,
                             KakaoLoginApi kakaoLoginApi, KakaoProfileApi kakaoProfileApi) {
        this.userService = userService;
        this.mapper = mapper;
        this.kakaoLoginApi = kakaoLoginApi;
        this.kakaoProfileApi = kakaoProfileApi;
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
    public KakaoLoginApi.Tokens newAccessToken(String loginApiResult) {
        log.info("[kakao login api result] loginApiResult: {}", loginApiResult);

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
        log.info("[kakao profile info] profileApiResult: {}", profileApiResult);

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
