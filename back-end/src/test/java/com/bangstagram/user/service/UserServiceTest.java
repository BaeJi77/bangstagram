package com.bangstagram.user.service;

import com.bangstagram.user.domain.model.user.User;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;


import static org.hamcrest.core.Is.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserServiceTest {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserService userService;

    private String name;

    private String email;

    private String password;

    @BeforeAll
    void setUp() {
        name = "tester";
        email = "test@gmail.com";
        password = "test1234";
    }

    @Test
    @Order(1)
    void 회원가입을_하다() {
        logger.info("email: {}, name: {}, password: {}" , email, name, password);

        User user = userService.join(name,email,password);
        assertThat(user, is(notNullValue()));
        assertThat(user.getSeq(), is(notNullValue()));
        assertThat(user.getEmail(), is(email));

        logger.info("Inserted user: {}", user);
    }

    @Test
    @Order(2)
    void 회원가입시_이메일_중복조회한다() {
        boolean isExist = userService.findByEmail(email).isPresent();
        assertThat(isExist, is(false));

        User user = userService.findByEmail(email).get();
        assertThat(user, is(null));
        assertThat(user.getSeq(), is(null));
        assertThat(user.getEmail(), is(email));

        logger.info("Found by {} : {} ", email, user);
    }

}
