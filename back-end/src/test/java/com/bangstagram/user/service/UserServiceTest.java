package com.bangstagram.user.service;

import com.bangstagram.user.domain.model.api.request.JoinRequestDto;
import com.bangstagram.user.domain.model.api.response.JoinResponseDto;
import com.bangstagram.user.domain.model.user.User;
import com.bangstagram.user.domain.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;

@SpringBootTest
@Slf4j
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private String name;

    private String email;

    private String password;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        name = "tester";
        email = "test@gmail.com";
        password = "test1234";
    }

    @Test
    @DisplayName("회원가입을_하다")
    void testJoin() {
        JoinResponseDto response = userService.join(new JoinRequestDto(name,email,password));
        assertThat(response, is(notNullValue()));

        User user = response.getUser();
        assertThat(user.getSeq(), is(notNullValue()));
        assertThat(user.getEmail(), is(email));

        String jwtToken = response.getJwtToken();
        assertThat(jwtToken, is(notNullValue()));

        log.info("Inserted user: {}", user);
        log.info("Created jwtToken: {}", jwtToken);
    }

    @Test
    @DisplayName("이메일_중복조회한다")
    void isExistedEmail() {
        boolean isExist = userService.existsByEmail(email);
        assertThat(isExist, is(false));

        // 회원가입 -> 이메일 DB에 저장.
        userService.join(new JoinRequestDto(name,email,password));

        isExist = userService.existsByEmail(email);
        assertThat(isExist, is(true));
    }

    @Test
    @DisplayName("이메일을_조회한다")
    void testFindByEmail() {
        // 회원가입 -> 이메일 DB에 저장.
        userService.join(new JoinRequestDto(name,email,password));

        User user = userService.findByEmail(email);
        assertThat(user, (notNullValue()));
        assertThat(user.getSeq(), is(notNullValue()));
        assertThat(user.getName(), is(name));
        assertThat(user.getEmail(), is(email));
        assertThat(passwordEncoder.matches(password,user.getPassword()), is(true)); // 비밀번호 일치 확인

    }
}
