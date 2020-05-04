package com.bangstagram.user.domain.model.oauth.naver;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;

@Slf4j
public class NaverLoginApi {
    private final String clientId;

    private final String clientSecret;

    private final String url;

    public NaverLoginApi(String clientId, String clientSecret, String url) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.url = url;
    }

    /*** 네이버 아이디로 접근 토큰 발급 API
     * url   - nid.naver.com/oauth2.0/token
     * param - grant_type(발급:'authorization_code', 갱신:'REFRESH_TOKEN', 삭제: 'delete')
     *       - client_id(네이버 API 아이디)
     *       - client_secret(네이버 API 시크릿 키)
     *       - code(로그인 인증 요청 API 호출 성공 후 받은 인증코드 값)
     *       - state(사이트 간 요청 위조 공격 방지를 위해 애플리케이션에서 생성한 상태 토큰값)
     */
    public String getLoginApiUrl(String grantType, String code, String state) {
        return new StringBuilder(url)
                .append("?grant_type=" + grantType)
                .append("&client_id=" + clientId)
                .append("&client_secret=" + clientSecret)
                .append("&code=" + code)
                .append("&state=" + state)
                .toString();
    }

    static public class Tokens {
        private String accessToken;
        private String refreshToken;
        private String tokenType;
        private String expiresIn;

        public Tokens(String accessToken, String refreshToken, String tokenType, String expiresIn) {
            this.accessToken = accessToken;
            this.refreshToken = refreshToken;
            this.tokenType = tokenType;
            this.expiresIn = expiresIn;
        }

        public Tokens(String loginApiResult) {
            try {
                JSONObject jsonObject = new JSONObject(loginApiResult);
                this.accessToken = jsonObject.getString("access_token");
                this.refreshToken = jsonObject.getString("refresh_token");
                this.tokenType = jsonObject.getString("token_type");
                this.expiresIn = jsonObject.getString("expires_in");
            } catch (JSONException jsonException) {
                log.error("error message: {}", jsonException.getMessage());
            }
        }

        public String parseToken2Header() {
            return "Bearer " + accessToken;
        }
    }
}
