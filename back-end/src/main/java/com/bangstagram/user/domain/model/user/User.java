package com.bangstagram.user.domain.model.user;

import com.bangstagram.user.security.JWT;
import lombok.Getter;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

import static java.time.LocalDateTime.now;
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

@Entity
@Getter
public class User {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long seq;

    private String name;

    private String email;

    private String password;

    private int loginCount;

    private LocalDateTime lastLoginAt;

    private LocalDateTime createAt;

    public User() {} // jpa 사용시 기본생성자 필수

    public User(String name, String email, String password) {
        this(null, name, email, password, 0, null, null);
    }

    public User(Long seq, String name, String email, String password, int loginCount, LocalDateTime lastLoginAt, LocalDateTime createAt) {
        this.seq = seq;
        this.name = name;
        this.email = email;
        this.password = password;
        this.loginCount = loginCount;
        this.lastLoginAt = lastLoginAt;
        this.createAt = defaultIfNull(createAt,now());
    }

    public String newJwtToken(JWT jwt, String[] roles) {
        JWT.Claims claims = JWT.Claims.of(seq, name, email, roles);
        return jwt.newToken(claims);
    }
}
