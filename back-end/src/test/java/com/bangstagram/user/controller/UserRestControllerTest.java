package com.bangstagram.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserRestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("이메일_중복_확인하기")
    void checkExistedEmail() throws Exception {
        Map<String,String> map = new HashMap<>();
        map.put("email","sa01747@naver.com");

        ObjectMapper mapper = new ObjectMapper();
        mockMvc.perform(post("http://localhost:9090/users/exists").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(map)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("회원가입_하기")
    void join() throws Exception {
        Map<String,String> map = new HashMap<>();
        map.put("loginEmail","sa01747@naver.com");
        map.put("loginPassword","test1234");

        ObjectMapper mapper = new ObjectMapper();
        mockMvc.perform(post("http://localhost:9090/users/join").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(map)))
                .andExpect(status().isOk())
                .andDo(print());
    }
}