package com.bangstagram.user.domain.model.oauth.kakao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Getter
public class KakaoLoginApi {
    private final String clientId;

    private final String url;

    public KakaoLoginApi(String clientId, String url) {
        this.clientId = clientId;
        this.url = url;
    }

    /*** 카카오 아이디로 접근 토큰 발급 API
     * url   - kauth.kakao.com/oauth/token
     * param - grant_type('authorization_code' 고정)
     *       - client_id(카카오 API 아이디)
     *       - client_secret(보안 강화를 위한 카카오 API 시크릿 키)
     *       - code(로그인 인증 요청 API 호출 성공 후 받은 인증코드 값)
     *       - redirect_url(코드가 리다이렉트된 URI)
     */
    public String getLoginApiUrl(String code) {
        return new StringBuilder(url)
                .append("?grant_type=" + "authorization_code")
                .append("&client_id=" + clientId)
                .append("&code=" + code)
                .toString();
    }

    public String makeRequestBody(String code) throws JsonProcessingException {
        Map<String, String> bodyMap = new HashMap<>();
        bodyMap.put("grant_type", "authorization_code");
        bodyMap.put("client_id", clientId);
        bodyMap.put("code", code);
        bodyMap.put("redirect_uri", "http://localhost:9090/oauth/kakao");

        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(bodyMap);
    }

    static public class Tokens {
        private String accessToken;
        private String refreshToken;
        private String tokenType;
        private Long expiresIn;
        private String scope;

        public Tokens(String accessToken, String refreshToken, String tokenType, Long expiresIn, String scope) {
            this.accessToken = accessToken;
            this.refreshToken = refreshToken;
            this.tokenType = tokenType;
            this.expiresIn = expiresIn;
            this.scope = scope;
        }

        public Tokens(String loginApiResult) {
            try {
                org.json.JSONObject jsonObject = new org.json.JSONObject(loginApiResult);
                this.accessToken = jsonObject.getString("access_token");
                this.refreshToken = jsonObject.getString("refresh_token");
                this.tokenType = jsonObject.getString("token_type");
                this.expiresIn = jsonObject.getLong("expires_in");
                this.scope = jsonObject.getString("scope");
            } catch (JSONException jsonException) {
                log.error("error message: {}", jsonException.getMessage());
            }
        }

        public String parseToken2Header() {
            return "Bearer " + accessToken;
        }
    }
}
