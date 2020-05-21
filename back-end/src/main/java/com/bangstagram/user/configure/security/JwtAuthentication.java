package com.bangstagram.user.configure.security;

import com.bangstagram.user.domain.model.user.User;
import lombok.Getter;

import java.util.StringJoiner;

/***
 * author: Hyo-Jin Kim
 * Date: 2020.05.20
 */

@Getter
public class JwtAuthentication {
    private Long id;

    private String name;

    private String email;

    public JwtAuthentication(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public JwtAuthentication(User user) {
        this(user.getId(), user.getName(), user.getEmail());
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", JwtAuthentication.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("name='" + name + "'")
                .add("email='" + email + "'")
                .toString();
    }
}