package com.bangstagram.user.property;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class JwtProperty {
    @Value("${jwt.token.issuer}")
    private String issuer;

    @Value("${jwt.token.secret}")
    private String secret;

    @Value("${jwt.token.expirySeconds}")
    private int expirySeconds;
}
