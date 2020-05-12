package com.bangstagram.user.domain.model.user;

import com.bangstagram.user.configure.security.JWT;
import lombok.extern.slf4j.Slf4j;
import org.junit.Rule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Slf4j
class UserTest {
    private Long seq;

    private String name;

    private String email;

    private String password;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JWT jwt;

    @BeforeEach
    void setUp() {
        seq = 1L;
        name = "tester";
        email = "sa01747@naver.com";
        password = passwordEncoder.encode("test1234");

    }

    @Test
    void newJwtToken() {
        User user = User.builder()
                .seq(1L)
                .name("홍길동")
                .email("sa01747@naver.com")
                .password("test1234")
                .loginCount(0)
                .createAt(LocalDateTime.now())
                .build();
        String jwtToken = user.newJwtToken(jwt,new String[] {"USER_ROLE","MANAGER_ROLE"});

        JWT.Claims claims = jwt.verify(jwtToken); // jwt Token 복호화

        assertThat(claims.getEmail().equals(user.getEmail()), is(true));
        assertThat(claims.getName().equals(user.getName()), is(true));
        assertThat(claims.getUserKey().equals(user.getSeq()), is(true));
        assertThat(claims.getRoles()[0].equals("USER_ROLE"), is(true));
        assertThat(claims.getRoles()[1].equals("MANAGER_ROLE"), is(true));
    }

    @Test
    void login() {
        User user = new User(name,email,password);

        // 맞는 비밀번호
        boolean passwordPass = false;
        user.login(passwordEncoder,"test1234");
        passwordPass = true;
        assertThat(passwordPass, is(true));

        // 틀린 비밀번호 -> IllegalArgumentException 발생
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> user.login(passwordEncoder,"test4321"));

        assertEquals("Bad Creditials", thrown.getMessage());
    }

    @Test
    void afterLoginSuccess() {
        User user = User.builder()
                    .seq(1L)
                    .name(name)
                    .email(email)
                    .password(password)
                    .loginCount(0)
                    .lastLoginAt(LocalDateTime.of(LocalDate.of(2020,4,20), LocalTime.of(12,10,10,0)))
                    .createAt(LocalDateTime.of(LocalDate.of(2020,4,10), LocalTime.of(11,9,10,0)))
                    .build();

        int asisLoginCount = user.getLoginCount();
        LocalDateTime asisLastLoginAt = user.getLastLoginAt();
        assertThat(user.getLoginCount(), is(0));
        log.info("[beforeLogin] loginCount: {}, lastLoginAt: {}", user.getLoginCount(), user.getLastLoginAt());

        user.afterLoginSuccess(); // 로그인 후 loginCount + 1, lastLoginAt 그 시간으로

        assertThat(user.getLoginCount(), is(not(asisLoginCount)));
        assertThat(user.getLastLoginAt(), is(not(asisLastLoginAt)));
        log.info("[afterLoginSuccess] loginCount: {}, lastLoginAt: {}", user.getLoginCount(), user.getLastLoginAt());
    }
}