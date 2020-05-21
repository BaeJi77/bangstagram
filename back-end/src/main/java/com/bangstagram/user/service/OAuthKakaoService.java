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

import java.util.HashMap;
import java.util.Map;

/***
 * author: Hyo-Jin Kim
 * Date: 2020.05.08
 */

@Service
@Slf4j
public class OAuthKakaoService implements OAuthServiceImpl<KakaoLoginApi.Tokens, KakaoProfileApi.ProfileInfo> {
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
        // 1. 사용자 토큰 발급 요청 API 호출하여 access_token 가져오기
        String accessTokenHeader = getAccessTokenAsHeader(code);

        // 2. 사용자 정보 요청 API 호출하여 회원 정보 가져오기
        KakaoProfileApi.ProfileInfo profileInfo = getProfileInfo(accessTokenHeader);
        String email = profileInfo.getEmail();

        // 3. 로그인 인증(AuthenticationManager 거치지 않고, SecurityContextHolder에 Authentication 등록)
        AuthResponseDto authResponseDto = userService.authLogin(email);
        JwtAuthenticationToken authenticated
                = new JwtAuthenticationToken(authResponseDto.getUser().getId(), null, AuthorityUtils.createAuthorityList("ROLE_USER"));
        authenticated.setDetails(authResponseDto);
        SecurityContextHolder.getContext().setAuthentication(authenticated);

        return (AuthResponseDto) authenticated.getDetails();
    }

    private String getAccessTokenAsHeader(String code) {
        String loginApiUrl = kakaoLoginApi.getLoginApiUrl(code);

        String requestBody = kakaoLoginApi.buildRequestBody(code, mapper);
        log.info("[OAuthKakaoService login] requestBody: {}", requestBody);

        String loginApiResult = HttpUtils.postMethod(loginApiUrl, requestBody, "application/x-www-form-urlencoded;charset=utf-8");

        KakaoLoginApi.Tokens tokens = newAccessToken(loginApiResult);

        return tokens.parseToken2Header(); // Bearer ~
    }

    private KakaoProfileApi.ProfileInfo getProfileInfo(String accessTokenHeader) {
        String profileApiUrl = kakaoProfileApi.getUrl();

        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("Authorization", accessTokenHeader);

        String profileApiResult = HttpUtils.getMethod(profileApiUrl, requestHeaders, "application/json; charset=utf-8");

        return newProfileInfo(profileApiResult);
    }

    @Override
    public KakaoLoginApi.Tokens newAccessToken(String loginApiResult) {
        log.info("[kakao request access_token api result] loginApiResult: {}", loginApiResult);

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
    public KakaoProfileApi.ProfileInfo newProfileInfo(String profileApiResult) {
        log.info("[kakao ProfileInfo api] profileApiResult: {}", profileApiResult);

        KakaoProfileApi.ProfileInfo profileInfo = new KakaoProfileApi.ProfileInfo();
        try {
            JsonNode jsonNode = mapper.readTree(profileApiResult);
            profileInfo = KakaoProfileApi.ProfileInfo.builder()
                    .name(jsonNode.path("kakao_account").path("profile").path("nickname").textValue())
                    .email(jsonNode.path("kakao_account").path("email").textValue())
                    .build();

        } catch (JsonProcessingException e) {
            log.error("error message: {}", e.getMessage());
        }

        return profileInfo;
    }
}
