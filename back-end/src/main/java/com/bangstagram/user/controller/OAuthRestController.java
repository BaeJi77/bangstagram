package com.bangstagram.user.controller;

import com.bangstagram.user.domain.model.api.response.AuthResponseDto;
import com.bangstagram.user.service.OAuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class OAuthRestController {
    private final OAuthService oAuthService;

    public OAuthRestController(OAuthService oAuthService) {
        this.oAuthService = oAuthService;
    }

    /***
     *  네이버 아이디로 로그인 API
     *  url  - https://nid.naver.com/oauth2.0/authorize
     *
     *  클라이언트에서 요청 -> redirectURI 주소인 oauth/naver로 code, state 받아옴
     */
    @GetMapping("/oauth/naver")
    public AuthResponseDto authNaver(@RequestParam("code") String code,
                                     @RequestParam("state") String state) {
        log.info("code:{}, state: {}", code, state);

        return oAuthService.loginWithNaver(code, state);
    }

    /***
     *  카카오 아이디로 로그인 API
     *  url  - https://kauth.kakao.com/oauth/authorize
     *
     *  클라이언트에서 요청 -> redirectURI 주소인 oauth/kakao code 받아옴
     */
    @GetMapping("/oauth/kakao")
    public AuthResponseDto authKakao(@RequestParam("code") String code) {
        log.info("code: {}", code);

        return oAuthService.loginWithKakao(code);
    }
}
