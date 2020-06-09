package com.bangstagram.user.controller;

import com.bangstagram.user.domain.model.user.User;
import com.bangstagram.user.domain.repository.UserRepository;
import com.bangstagram.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static com.bangstagram.common.ApiDocumentUtils.getDocumentRequest;
import static com.bangstagram.common.ApiDocumentUtils.getDocumentResponse;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/***
 * author: Hyo-Jin Kim
 * Date: 2020.05.13
 */

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureRestDocs(uriScheme = "http", uriHost = "docs.api.com")
class UserRestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach()
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("이메일_중복_확인하기")
    void checkExistedEmail() throws Exception {
        // given
        join();

        Map<String,String> request = new HashMap<>();
        request.put("email","sa01747@naver.com");

        // when
        ObjectMapper mapper = new ObjectMapper();
        ResultActions result = mockMvc.perform(post("/users/exists")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)));

        // then
        result.andExpect(status().isOk())
                .andDo(document("/users/exists",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestFields(
                                fieldWithPath("email").type(JsonFieldType.STRING).description("이메일")
                        ),
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.BOOLEAN).description("이메일 중복 유무 결과")
                        )
                ))
                .andDo(print());
    }

    @Test
    @DisplayName("회원가입_하기")
    void join() throws Exception {
        // given
        User user = User.builder()
                .id(1L)
                .name("테스터")
                .email("sa01747@naver.com")
                .password("test1234")
                .loginCount(0)
                .createAt(LocalDateTime.now())
                .build();

        // when
        Map<String,String> joinRequestDto = new HashMap();
        joinRequestDto.put("name",user.getName());
        joinRequestDto.put("email",user.getEmail());
        joinRequestDto.put("password",user.getPassword());

        ResultActions result = mockMvc.perform(post("/users/join")
                .content(objectMapper.writeValueAsString(joinRequestDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isOk())
                .andDo(document("/users/join",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestFields(
                                fieldWithPath("name").type(JsonFieldType.STRING).description("이름"),
                                fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                                fieldWithPath("password").type(JsonFieldType.STRING).description("패스워드")
                        ),
                        responseFields(
                                beneathPath("user").withSubsectionId("user"),
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("아이디"),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("이름"),
                                fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                                fieldWithPath("loginCount").type(JsonFieldType.NUMBER).description("로그인성공횟수"),
                                fieldWithPath("lastLoginAt").type(JsonFieldType.NULL).description("마지막로그인날짜"),
                                fieldWithPath("createAt").type(JsonFieldType.ARRAY).description("생성날짜")
                        )
                ))
                .andDo(print());
    }

    @Test
    @DisplayName("로그인_하기")
    void login() throws Exception {
        // given
        User user = User.builder()
                .id(1L)
                .name("테스터")
                .email("sa01747@naver.com")
                .password("test1234")
                .loginCount(0)
                .createAt(LocalDateTime.now())
                .build();

        // when
        join(); // 회원가입

        Map<String, String> requestBody = new HashMap();
        requestBody.put("principal", user.getEmail());
        requestBody.put("credentials", user.getPassword());

        ResultActions result = mockMvc.perform(post("/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody)));

        // then
        result.andExpect(status().isOk())
                .andDo(document("/users/login",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestFields(
                                fieldWithPath("principal").type(JsonFieldType.STRING).description("이메일"),
                                fieldWithPath("credentials").type(JsonFieldType.STRING).description("패스워드")
                        ),
                        responseFields(
                                fieldWithPath("jwtToken").type(JsonFieldType.STRING).description("JWT토큰"),
                                fieldWithPath("user.id").type(JsonFieldType.NUMBER).description("아이디"),
                                fieldWithPath("user.name").type(JsonFieldType.STRING).description("이름"),
                                fieldWithPath("user.email").type(JsonFieldType.STRING).description("이메일"),
                                fieldWithPath("user.loginCount").type(JsonFieldType.NUMBER).description("로그인성공횟수"),
                                fieldWithPath("user.lastLoginAt").type(JsonFieldType.ARRAY).description("마지막로그인날짜"),
                                fieldWithPath("user.createAt").type(JsonFieldType.ARRAY).description("생성날짜")
                        )
                ))
                .andDo(print());
    }
}