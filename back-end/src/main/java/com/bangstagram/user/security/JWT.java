package com.bangstagram.user.security;

import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.Getter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Arrays;
import java.util.Date;

@Getter
public class JWT {
    private final String issuer;
    private final String secret;
    private final int expirySeconds;
    private final Algorithm algorithm;
    private final JWTVerifier jwtVerifier;

    public JWT(String issuer, String secret, int expirySeconds) {
        this.issuer = issuer;
        this.secret = secret;
        this.expirySeconds = expirySeconds;
        this.algorithm = Algorithm.HMAC512(secret);
        this.jwtVerifier = com.auth0.jwt.JWT.require(algorithm)
                .withIssuer(issuer)
                .build();
    }

    public String newToken(Claims claims) {
        Date now = new Date();
        JWTCreator.Builder builder = com.auth0.jwt.JWT.create();

        builder.withIssuer(issuer);
        builder.withIssuedAt(now);
        if( expirySeconds > 0 )
            builder.withExpiresAt(new Date(now.getTime() * expirySeconds * 1000L)); // 만료일
        builder.withClaim("userKey", claims.userKey);
        builder.withClaim("name", claims.name);
        builder.withClaim("email", claims.email);
        builder.withArrayClaim("roles", claims.roles);

        return builder.sign(algorithm);
    }

    static public class Claims {
        private Long userKey;
        private String name;
        private String email;
        private String[] roles;
        private Date issuedAt;
        private Date expiresAt;

        private Claims() {}

        public static Claims of(Long userKey, String name, String email, String[] roles) {
            Claims claims = new Claims();
            claims.userKey = userKey;
            claims.name = name;
            claims.email = email;
            claims.roles = roles;
            return claims;
        }

        long iat() {
            return issuedAt != null ? issuedAt.getTime() : -1;
        }

        long exp() {
            return expiresAt != null ? expiresAt.getTime() : -1;
        }

        void eraseIat() {
            issuedAt = null;
        }

        void eraseExp() {
            expiresAt = null;
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                    .append("userKey", userKey)
                    .append("name", name)
                    .append("email", email)
                    .append("roles", Arrays.toString(roles))
                    .append("issuedAt", issuedAt)
                    .append("expiresAt", expiresAt)
                    .toString();
        }
    }


}
