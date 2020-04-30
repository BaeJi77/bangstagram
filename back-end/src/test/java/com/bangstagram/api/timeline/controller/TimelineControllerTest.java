package com.bangstagram.api.timeline.controller;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TimelineControllerTest {
    @Autowired
    private MockMvc mockMvc;

    private static final String TIMELINE_URL_PATH = "/timelines";

    @Test
    @DisplayName("(성공) 타임라인 만들기: 모든 데이터 잘 들어갔을 때")
    public void isSuccessCreateTimelineWithGoodData() throws Exception {
        String goodJsonData = "{\n\t\"title\": \"hoon\",\n\t\"body\": \"hoon\",\n\t\"userId\": 1,\n\t\"roomId\": 1\n}";
        mockMvc.perform(post(TIMELINE_URL_PATH).contentType(MediaType.APPLICATION_JSON).content(goodJsonData))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    @DisplayName("(실패) 타임라인 만들기: Title 없을 때")
    public void isFailCreateTimelineWithoutTitle() throws Exception {
        String jsonDataWithoutTitle = "{\"body\": \"hoon\",\n\t\"userId\": 1,\n\t\"roomId\": 1\n}";
        mockMvc.perform(post(TIMELINE_URL_PATH).contentType(MediaType.APPLICATION_JSON).content(jsonDataWithoutTitle))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("(실패) 타임라인 만들기: Body 없을 때")
    public void isFailCreateTimelineWithoutBody() throws Exception {
        String jsonDataWithoutBody = "{\n\t\"title\": \"hoon\",\n\t\"userId\": 1,\n\t\"roomId\": 1\n}";
        mockMvc.perform(post(TIMELINE_URL_PATH).contentType(MediaType.APPLICATION_JSON).content(jsonDataWithoutBody))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("(실패) 타임라인 만들기: UserId 없을 때")
    public void isFailCreateTimelineWithoutUserId() throws Exception {
        String jsonDataWithoutUserId = "{\n\t\"title\": \"hoon\",\n\t\"body\": \"hoon\",\n\t\"roomId\": 1\n}";
        mockMvc.perform(post(TIMELINE_URL_PATH).contentType(MediaType.APPLICATION_JSON).content(jsonDataWithoutUserId))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("(실패) 타임라인 만들기: RoomId 없을 때")
    public void isFailCreateTimelineWithoutRoomId() throws Exception {
        String jsonDataWithoutRoomId = "{\n\t\"title\": \"hoon\",\n\t\"body\": \"hoon\",\n\t\"userId\": 1}";
        mockMvc.perform(post(TIMELINE_URL_PATH).contentType(MediaType.APPLICATION_JSON).content(jsonDataWithoutRoomId))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }
}
