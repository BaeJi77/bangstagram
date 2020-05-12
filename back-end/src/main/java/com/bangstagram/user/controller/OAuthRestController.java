package com.bangstagram.user.controller;

import com.bangstagram.user.controller.dto.response.AuthResponseDto;
import com.bangstagram.user.service.OAuthKakaoService;
import com.bangstagram.user.service.OAuthNaverService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class OAuthRestController {
    private final OAuthNaverService oAuthNaverService;

    private final OAuthKakaoService oAuthKakaoService;

    public OAuthRestController(OAuthNaverService oAuthNaverService, OAuthKakaoService oAuthKakaoService) {
        this.oAuthNaverService = oAuthNaverService;
        this.oAuthKakaoService = oAuthKakaoService;
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

        return oAuthNaverService.login(code, state);
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

        return oAuthKakaoService.login(code);
    }
}
