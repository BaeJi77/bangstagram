package com.bangstagram.user.configure.security;

import com.bangstagram.user.domain.model.user.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/***
 * author: Hyo-Jin Kim
 * Date: 2020.05.20
 */

@SpringBootTest
@Slf4j
class JwtAuthenticationTest {
    private Long id;

    private String name;

    private String email;

    @BeforeEach
    void setUp() {
        id = 1L;
        name = "name";
        email = "test@test.com";
    }

    @Test
    void testToString() {
        User user = User.builder()
                .id(id)
                .name(name)
                .email(email)
                .password("test1234")
                .loginCount(0)
                .createAt(LocalDateTime.now())
                .build();

        JwtAuthentication jwtAuthentication = new JwtAuthentication(id, name, email);
        JwtAuthentication jwtAuthenticationByUser = new JwtAuthentication(user);

        assertThat(jwtAuthentication.getId(), is(jwtAuthenticationByUser.getId()));
        assertThat(jwtAuthentication.getName(), is(jwtAuthenticationByUser.getName()));
        assertThat(jwtAuthentication.getEmail(), is(jwtAuthenticationByUser.getEmail()));

        log.info("jwtAuthentication: {}", jwtAuthentication.toString());
        log.info("jwtAuthenticationByUser: {}", jwtAuthenticationByUser.toString());
    }
}