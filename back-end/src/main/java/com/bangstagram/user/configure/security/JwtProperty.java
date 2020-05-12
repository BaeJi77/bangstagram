package com.bangstagram.user.configure.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

/***
 * author: Hyo-Jin Kim
 * Date: 2020.05.09
 */

@Setter
@Getter
@ConfigurationProperties(prefix = "jwt")
public class JwtProperty {
    @Value("${jwt.token.issuer}")
    private String issuer;

    @Value("${jwt.token.secret}")
    private String secret;

    @Value("${jwt.token.expirySeconds}")
    private int expirySeconds;
}
