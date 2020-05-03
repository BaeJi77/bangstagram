package com.bangstagram.user.controller;

import com.bangstagram.user.domain.model.api.response.AuthResponseDto;
import com.bangstagram.user.service.OAuthService;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
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

    @GetMapping("auth/naver")
    public AuthResponseDto auth(@RequestParam("code") String code,
                                @RequestParam("state") String state) throws ParseException {
        log.info("code:{}, state: {}", code, state);

        /***
         *  네이버 아이디로 로그인 API
         *  url  - nid.naver.com/oauth2.0/authorize
         *
         *  클라이언트에서 요청 -> redirectURI 주소인 auth/naver로 code, state를 받아옴
         */

        return oAuthService.loginWithNaver(code, state);
    }
}
