package com.bangstagram.user.domain.model.user;

import com.bangstagram.user.configure.security.JWT;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Getter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long seq;

    private String name;

    private String email;

    @JsonIgnore
    private String password;

    private int loginCount;

    private LocalDateTime lastLoginAt;

    private LocalDateTime createAt;

    private String oAuth;

    public User() {
    } // jpa 사용시 기본생성자 필수

    public User(String name, String email, String password, String oAuth) {
        this(null, name, email, password, 0, null, null, oAuth);
    }

    @Builder
    public User(Long seq, String name, String email, String password, int loginCount, LocalDateTime lastLoginAt, LocalDateTime createAt, String oAuth) {
        this.seq = seq;
        this.name = name;
        this.email = email;
        this.password = password;
        this.loginCount = loginCount;
        this.lastLoginAt = lastLoginAt;
        this.createAt = ObjectUtils.defaultIfNull(createAt, LocalDateTime.now());
        this.oAuth = oAuth;
    }

    public String newJwtToken(JWT jwt, String[] roles) {
        JWT.Claims claims = JWT.Claims.of(seq, name, email, roles);
        return jwt.newToken(claims);
    }

    public void login(PasswordEncoder passwordEncoder, String password) {
        if (!passwordEncoder.matches(password, this.password))
            throw new IllegalArgumentException("Bad Creditials");
    }

    public void afterLoginSuccess() {
        this.loginCount++;
        lastLoginAt = LocalDateTime.now();
    }
}
