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

/***
 * author: Hyo-Jin Kim
 * Date: 2020.05.08
 */

@Entity
@Getter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private String email;

    @JsonIgnore
    private String password;

    private int loginCount;

    private LocalDateTime lastLoginAt;

    private LocalDateTime createAt;

    public User() {
    } // jpa 사용시 기본생성자 필수

    public User(String name, String email, String password) {
        this(null, name, email, password, 0, null, null);
    }

    @Builder
    public User(Long id, String name, String email, String password, int loginCount, LocalDateTime lastLoginAt, LocalDateTime createAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.loginCount = loginCount;
        this.lastLoginAt = lastLoginAt;
        this.createAt = ObjectUtils.defaultIfNull(createAt, LocalDateTime.now());
    }

    public String newJwtToken(JWT jwt, String[] roles) {
        JWT.Claims claims = JWT.Claims.of(id, name, email, roles);
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
