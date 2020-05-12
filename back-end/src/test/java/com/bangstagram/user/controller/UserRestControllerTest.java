package com.bangstagram.user.controller;

import com.bangstagram.user.configure.security.JWT;
import com.bangstagram.user.controller.dto.request.AuthRequestDto;
import com.bangstagram.user.controller.dto.response.AuthResponseDto;
import com.bangstagram.user.domain.model.user.User;
import com.bangstagram.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserRestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private JWT jwt;

    @Test
    @DisplayName("이메일_중복_확인하기")
    void checkExistedEmail() throws Exception {
        join();
        Map<String,String> map = new HashMap<>();
        map.put("email","sa01747@naver.com");

        given(userService.existsByEmail(map.get("email"))).willReturn(true);

        ObjectMapper mapper = new ObjectMapper();
        MvcResult result = mockMvc.perform(post("/users/exists")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(map)))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        String results = result.getResponse().getContentAsString();
        assertTrue(Boolean.parseBoolean(results));
    }

    @Test
    @DisplayName("회원가입_하기")
    void join() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name","홍길동");
        jsonObject.put("email","sa01747@naver.com");
        jsonObject.put("password","test1234");

        mockMvc.perform(post("/users/join")
                .content(jsonObject.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
    }

    @Test
    @DisplayName("로그인_하기")
    void login() throws Exception {
        User user = User.builder()
                .seq(1L)
                .name("홍길동")
                .email("sa01747@naver.com")
                .password("test1234")
                .loginCount(0)
                .createAt(LocalDateTime.now())
                .build();
        String jwtToken = user.newJwtToken(jwt, new String[] {"USER_ROLE"});

        AuthRequestDto authRequestDto = new AuthRequestDto("sa01747@naver.com","test1234");
        given(userService.login(authRequestDto)).willReturn(new AuthResponseDto(user,jwtToken));

        JSONObject requestJSON = new JSONObject();
        requestJSON.put("principal", "sa01747@naver.com");
        requestJSON.put("credentials", "test1234");
        mockMvc.perform(post("/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJSON.toString()))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
    }
}