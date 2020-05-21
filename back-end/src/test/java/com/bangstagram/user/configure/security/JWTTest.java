package com.bangstagram.user.configure.security;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

/***
 * author: Hyo-Jin Kim
 * Date: 2020.05.05
 */

@SpringBootTest
public class JWTTest {
    @Autowired
    private JWT jwt;

    @BeforeEach
    void setUp() {
        String issuer = "bangstagram-server";
        String clientSecret = "h9y2o0z2k0i8m";
        int expirySeconds = 10;

        jwt = new JWT(issuer, clientSecret, expirySeconds);
    }

    @Test
    void JWT_토큰을_생성하고_복호화_하다() {
        JWT.Claims claims = JWT.Claims.of(1L,"tester", "test@gmail.com", new String[]{"ROLE_USER"});
        String encodedJWT = jwt.newToken(claims);

        JWT.Claims decodedJWT = jwt.verify(encodedJWT);

        assertThat(claims.getUserKey(), is(decodedJWT.getUserKey()));
        assertArrayEquals(claims.getRoles(), decodedJWT.getRoles());
    }
}
