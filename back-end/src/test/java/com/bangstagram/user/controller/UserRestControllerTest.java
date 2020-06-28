package com.bangstagram.user.controller;

import com.bangstagram.user.controller.dto.request.AuthRequestDto;
import com.bangstagram.user.controller.dto.request.JoinRequestDto;
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
@AutoConfigureRestDocs
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
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
        JoinRequestDto joinRequestDto = new JoinRequestDto("name","sa01747@naver.com", "test1234");
        userService.join(joinRequestDto); //join

        // when
        Map<String,String> request = new HashMap<>();
        request.put("email","sa01747@naver.com");

        ResultActions result = mockMvc.perform(post("/users/exists")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        result.andExpect(status().isOk())
                .andDo(document("users/exists",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestFields(
                                fieldWithPath("email").type(JsonFieldType.STRING).description("이메일")
                        ),
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.BOOLEAN).description("이메일 중복 유무")
                        )
                ))
                .andDo(print());
    }

    @Test
    @DisplayName("회원가입_하기")
    void join() throws Exception {
        // when
        JoinRequestDto joinRequestDto = new JoinRequestDto("테스터", "sa01747@naver.com", "test1234");
        ResultActions result = mockMvc.perform(post("/users/join")
                .content(objectMapper.writeValueAsString(joinRequestDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isOk())
                .andDo(document("users/join",
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
    @DisplayName("회원가입_하기: 이미 사용자 이메일이 존재하는 경우")
    public void isAlreadyExistUserCheck() throws Exception {
        // given
        JoinRequestDto joinRequestDto = new JoinRequestDto("테스터", "sa01747@naver.com", "test1234");
        userService.join(joinRequestDto); //join

        // when
        mockMvc.perform(post("/users/join")
                .content(objectMapper.writeValueAsString(joinRequestDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andDo(print());
    }

    @Test
    @DisplayName("로그인_하기")
    void login() throws Exception {
        // given
        JoinRequestDto joinRequestDto = new JoinRequestDto("테스터", "sa01747@naver.com", "test1234");
        userService.join(joinRequestDto); // join

        // when
        AuthRequestDto authRequestDto = new AuthRequestDto(joinRequestDto.getEmail(), joinRequestDto.getPassword());
        ResultActions result = mockMvc.perform(post("/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authRequestDto)));

        // then
        result.andExpect(status().isOk())
                .andDo(document("users/login",
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