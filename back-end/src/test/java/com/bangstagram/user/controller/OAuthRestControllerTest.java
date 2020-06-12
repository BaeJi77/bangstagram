package com.bangstagram.user.controller;

import com.bangstagram.user.configure.security.JWT;
import com.bangstagram.user.controller.dto.response.AuthResponseDto;
import com.bangstagram.user.domain.model.user.User;
import com.bangstagram.user.service.OAuthKakaoService;
import com.bangstagram.user.service.OAuthNaverService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;

import static com.bangstagram.common.ApiDocumentUtils.getDocumentRequest;
import static com.bangstagram.common.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/***
 * author: Hyo-Jin Kim
 * Date: 2020.05.13
 */

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureRestDocs(uriScheme = "http", uriHost = "docs.api.com")
class OAuthRestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OAuthNaverService oAuthNaverService;

    @MockBean
    private OAuthKakaoService oAuthKakaoService;

    @Autowired
    private JWT jwt;

    @Test
    @DisplayName("네이버_소셜로그인_하기")
    void authNaver() throws Exception {
        // given

        // 1. 네이버 아이디로 회원가입
        User user = User.builder()
                .id(1L)
                .name("테스터")
                .email("sa01747@naver.com")
                .password("test1234")
                .loginCount(0)
                .createAt(LocalDateTime.now())
                .build();
        String jwtToken = user.newJwtToken(jwt, new String[] {"USER_ROLE"});

        // 2. 네이버 소셜 로그인
        //String exCode = "i1JdPz2H5abJBQB18e";
        //String exState = "616e13a8-c394-443f-aac9-9170d4d32dbc";
        String exCode = "example code";
        String exState = "example state";
        user.afterLoginSuccess();
        given(oAuthNaverService.login(exCode,exState)).willReturn(new AuthResponseDto(user,jwtToken));

        ResultActions result = mockMvc.perform(get("/oauth/naver")
                .contentType(MediaType.APPLICATION_JSON)
                .param("code", exCode)
                .param("state", exState));

        // when
        when(oAuthNaverService.login(exCode,exState)).thenReturn(new AuthResponseDto(user,jwtToken));

        // then
        result.andExpect(status().isOk())
                .andDo(document("/oauth/naver",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestParameters(
                                parameterWithName("code").description("네이버 인증 코드"),
                                parameterWithName("state").description("네이버 인증 상태")
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

    @Test
    @DisplayName("카카오_소셜로그인_하기")
    void authKakao() throws Exception {
        // 1. 카카오 아이디로 회원가입
        User user = User.builder()
                .id(1L)
                .name("테스터")
                .email("sa833591@gmail.com")
                .password("test1234")
                .loginCount(0)
                .createAt(LocalDateTime.now())
                .build();
        String jwtToken = user.newJwtToken(jwt, new String[] {"USER_ROLE"});

        // 2. 카카오 소셜 로그인
        //String exCode = "i1JdPz2H5abJBQB18e";
        String exCode = "example code";
        user.afterLoginSuccess();
        given(oAuthKakaoService.login(exCode)).willReturn(new AuthResponseDto(user,jwtToken));

        ResultActions result = mockMvc.perform(get("/oauth/kakao")
                .contentType(MediaType.APPLICATION_JSON)
                .param("code",exCode));

        result.andExpect(status().isOk())
                .andDo(document("/oauth/kakao",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestParameters(
                                parameterWithName("code").description("카카오 인증 코드")
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