package com.bangstagram.user.domain.model.user;

import com.bangstagram.user.security.JWT;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.StringJoiner;

import static com.google.common.base.Preconditions.checkArgument;
import static java.time.LocalDateTime.now;
import static java.util.regex.Pattern.matches;
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

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

    public User() {} // findByEmail 시 기본생성자 필수

    public User(String name, String email, String password) {
        this(null, name, email, password, 0, null, null);
    }

    public User(Long seq, String name, String email, String password, int loginCount, LocalDateTime lastLoginAt, LocalDateTime createAt) {
        checkArgument(isNotEmpty(email), "address must be provided.");
        checkArgument(
                email.length() >= 4 && email.length() <= 50,
                "address length must be between 4 and 50 characters."
        );
        checkArgument(checkAddress(email), "Invalid email address: " + email);
        checkArgument(isNotEmpty(password), "password must be provided.");

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

    private static boolean checkAddress(String address) {
        return matches("[\\w~\\-.+]+@[\\w~\\-]+(\\.[\\w~\\-]+)+", address);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", User.class.getSimpleName() + "[", "]")
                .add("seq=" + seq)
                .add("name='" + name + "'")
                .add("email='" + email + "'")
                .add("password='" + password + "'")
                .add("loginCount=" + loginCount)
                .add("lastLoginAt=" + lastLoginAt)
                .add("createAt=" + createAt)
                .toString();
    }
}
