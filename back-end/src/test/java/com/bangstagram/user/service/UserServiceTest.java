package com.bangstagram.user.service;

import com.bangstagram.user.controller.dto.request.AuthRequestDto;
import com.bangstagram.user.controller.dto.request.JoinRequestDto;
import com.bangstagram.user.controller.dto.response.AuthResponseDto;
import com.bangstagram.user.controller.dto.response.JoinResponseDto;
import com.bangstagram.user.domain.model.user.User;
import com.bangstagram.user.domain.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.*;

/***
 * author: Hyo-Jin Kim
 * Date: 2020.05.05
 */

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
        email = "test@naver.com";
        password = "test1234";
    }

    @Test
    @DisplayName("회원가입을_하다")
    void join() {
        JoinResponseDto response = userService.join(new JoinRequestDto(name, email, password));
        assertThat(response, is(notNullValue()));

        User user = response.getUser();
        assertThat(user.getId(), is(notNullValue()));
        assertThat(user.getEmail(), is(email));

        log.info("Inserted user: {}", user);
    }

    @Test
    @DisplayName("이메일_중복조회한다")
    void isExistedEmail() {
        boolean isExist = userService.existsByEmail(email);
        assertThat(isExist, is(false));

        // 회원가입 -> 이메일 DB에 저장.
        userService.join(new JoinRequestDto(name, email, password));

        isExist = userService.existsByEmail(email);
        assertThat(isExist, is(true));
    }

    @Test
    @DisplayName("이메일을_조회한다")
    void findByEmail() {
        // 회원가입 -> 이메일 DB에 저장.
        userService.join(new JoinRequestDto(name, email, password));

        User user = userService.findByEmail(email);
        assertThat(user, (notNullValue()));
        assertThat(user.getId(), is(notNullValue()));
        assertThat(user.getName(), is(name));
        assertThat(user.getEmail(), is(email));
        assertThat(passwordEncoder.matches(password, user.getPassword()), is(true)); // 비밀번호 일치 확인

    }

    @Test
    @DisplayName("이메일_패스워드로_로그인_한다")
    void login() {
        // 1. 회원가입 -> 이메일 DB에 저장.
        JoinResponseDto joinResponseDto = userService.join(new JoinRequestDto(name, email, password));
        User user = joinResponseDto.getUser();

        // 2. 로그인(성공)
        AuthRequestDto success_authRequestDto = new AuthRequestDto(email, password);
        AuthResponseDto authResponseDto = userService.login(success_authRequestDto);
        User afterLoginUser = authResponseDto.getUser();
        String afterLoginJwtToken = authResponseDto.getJwtToken();
        assertThat(user.getLoginCount(), is(not(afterLoginUser.getLoginCount())));
        assertThat(user.getLastLoginAt(), is(not(afterLoginUser.getLastLoginAt())));
        // assertThat(jwtToken.equals(afterLoginJwtToken), true);
        log.info("[After Login Success] afterLoginJwtToken: {}", afterLoginJwtToken);

        // 3. 로그인(패스워드 불일치)
        AuthRequestDto fail_authRequestDto = new AuthRequestDto(email, "wrongPasssword");
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> userService.login(fail_authRequestDto));

        assertEquals("Bad Creditials", thrown.getMessage());
    }

    @Test
    @DisplayName("소셜_아이디로_로그인_한다")
    void authLogin() {
        // 1. 회원가입 -> 이메일 DB에 저장.
        JoinResponseDto joinResponseDto = userService.join(new JoinRequestDto(name, email, password));
        User user = joinResponseDto.getUser();

        // 2. 로그인(성공)
        AuthResponseDto authResponseDto = userService.authLogin(email);
        User afterLoginUser = authResponseDto.getUser();
        String afterLoginJwtToken = authResponseDto.getJwtToken();
        assertThat(user.getLoginCount(), is(not(afterLoginUser.getLoginCount())));
        assertThat(user.getLastLoginAt(), is(not(afterLoginUser.getLastLoginAt())));
        // assertThat(jwtToken.equals(afterLoginJwtToken), true);
        log.info("[After Login Success] afterLoginJwtToken: {}", afterLoginJwtToken);
    }
}
