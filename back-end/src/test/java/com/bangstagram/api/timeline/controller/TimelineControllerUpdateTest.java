package com.bangstagram.api.timeline.controller;


import com.bangstagram.timeline.dto.TimelineResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
    author: Ji-Hoon Bae
    Date: 2020.04.28
 */

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TimelineControllerUpdateTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    private static final String TIMELINE_URL_PATH = "/timelines";
    private static final MediaType JSON_MEDIA_TYPE = MediaType.APPLICATION_JSON;
    private String requestUrlPath;

    @BeforeEach
    void beforeEach() throws Exception {
        String goodJsonData = "{\"title\": \"hoon\",\n\t\"body\": \"hoon\",\n\t\"userId\": 1,\n\t\"roomId\": 1\n}";
        MvcResult result = mockMvc.perform(post(TIMELINE_URL_PATH).contentType(JSON_MEDIA_TYPE).content(goodJsonData))
                .andReturn();
        TimelineResponseDto newTimeline
                = mapper.readValue(result.getResponse().getContentAsString(), TimelineResponseDto.class);
        requestUrlPath = TIMELINE_URL_PATH + "/" + newTimeline.getId();
    }

    @Test
    public void isFailDoNotExistTimelineId() throws Exception {
        String doNotExistTimeline = TIMELINE_URL_PATH + "/987654321";
        String goodJsonData = "{\"title\": \"new\",\n\t\"body\": \"new\"}";
        mockMvc.perform(put(doNotExistTimeline).contentType(JSON_MEDIA_TYPE).content(goodJsonData))
                .andExpect(status().is4xxClientError())
                .andDo(print());
    }

    @Test
    public void isFailUpdateTimelineWithoutTitle() throws Exception {
        String jsonDataWithoutTitle = "{\"body\": \"hoon\"}";
        mockMvc.perform(put(requestUrlPath).contentType(JSON_MEDIA_TYPE).content(jsonDataWithoutTitle))
                .andExpect(status().is4xxClientError())
                .andDo(print());
    }

    @Test
    public void isFailUpdateTimelineWithoutBody() throws Exception {
        String jsonDataWithoutBody = "{\"title\": \"new\"}";
        mockMvc.perform(put(requestUrlPath).contentType(JSON_MEDIA_TYPE).content(jsonDataWithoutBody))
                .andExpect(status().is4xxClientError())
                .andDo(print());
    }

    @Test
    public void isSuccessUpdateTimeline() throws Exception {
        String goodJsonData = "{\"title\": \"hoon\",\n\t\"body\": \"hoon\"}";
        mockMvc.perform(put(requestUrlPath).contentType(JSON_MEDIA_TYPE).content(goodJsonData))
                .andExpect(status().is2xxSuccessful())
                .andDo(print());
    }
}
