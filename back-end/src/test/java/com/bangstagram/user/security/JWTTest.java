package com.bangstagram.user.security;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JWTTest {
    private Logger log = LoggerFactory.getLogger(getClass());

    private JWT jwt;

    @BeforeAll
    void setUp() {
        String issuer = "bangstagram-server";
        String clientSecret = "h9y2o0z2k0i8m";
        int expirySeconds = 10;

        jwt = new JWT(issuer, clientSecret, expirySeconds);
    }

    @Test
    void JWT_토큰을_생성할수있다() {
        JWT.Claims claims = JWT.Claims.of(1L,"tester", "test@gmail.com", new String[]{"ROLE_USER"});
        String encodedJWT = jwt.newToken(claims);
        log.info("encodedJWT: {}", encodedJWT);
    }
}
