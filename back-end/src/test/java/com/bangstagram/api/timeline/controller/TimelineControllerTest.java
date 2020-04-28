package com.bangstagram.api.timeline.controller;


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

    private static final String URL_PATH = "/timelines";
    private static final MediaType JSON_MEDIA_TYPE = MediaType.APPLICATION_JSON;

    @Test
    public void isSuccessCreateTimelineWithGoodData() throws Exception {
        String goodJsonData = "{\n\t\"title\": \"hoon\",\n\t\"body\": \"hoon\",\n\t\"userId\": 1,\n\t\"roomId\": 1\n}";
        mockMvc.perform(post(URL_PATH).contentType(JSON_MEDIA_TYPE).content(goodJsonData))
                .andExpect(status().is2xxSuccessful())
                .andDo(print());
    }

    @Test
    public void isFailCreateTimelineWithoutTitle() throws Exception {
        String jsonDataWithoutTitle = "{\"body\": \"hoon\",\n\t\"userId\": 1,\n\t\"roomId\": 1\n}";
        mockMvc.perform(post(URL_PATH).contentType(JSON_MEDIA_TYPE).content(jsonDataWithoutTitle))
                .andExpect(status().is4xxClientError())
                .andDo(print());
    }

    @Test
    public void isFailCreateTimelineWithoutBody() throws Exception {
        String jsonDataWithoutBody = "{\n\t\"title\": \"hoon\",\n\t\"userId\": 1,\n\t\"roomId\": 1\n}";
        mockMvc.perform(post(URL_PATH).contentType(JSON_MEDIA_TYPE).content(jsonDataWithoutBody))
                .andExpect(status().is4xxClientError())
                .andDo(print());
    }

    @Test
    public void isFailCreateTimelineWithoutUserId() throws Exception {
        String jsonDataWithoutUserId = "{\n\t\"title\": \"hoon\",\n\t\"body\": \"hoon\",\n\t\"roomId\": 1\n}";
        mockMvc.perform(post(URL_PATH).contentType(JSON_MEDIA_TYPE).content(jsonDataWithoutUserId))
                .andExpect(status().is4xxClientError())
                .andDo(print());
    }

    @Test
    public void isFailCreateTimelineWithoutRoomId() throws Exception {
        String jsonDataWithoutRoomId = "{\n\t\"title\": \"hoon\",\n\t\"body\": \"hoon\",\n\t\"userId\": 1}";
        mockMvc.perform(post(URL_PATH).contentType(JSON_MEDIA_TYPE).content(jsonDataWithoutRoomId))
                .andExpect(status().is4xxClientError())
                .andDo(print());
    }
}
