package com.bangstagram.timeline.controller;


import com.bangstagram.timeline.dto.TimelineResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * author: Ji-Hoon Bae
 * Date: 2020.04.28
 */

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TimelineControllerUpdateTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    private static final String TIMELINE_URL_PATH = "/timelines";
    private String requestUrlPath;

    @BeforeEach
    void setUp() throws Exception {
        String goodJsonData = "{\"title\": \"hoon\",\n\t\"body\": \"hoon\",\n\t\"userId\": 1,\n\t\"roomId\": 1\n}";
        MvcResult result = mockMvc.perform(post(TIMELINE_URL_PATH).contentType(MediaType.APPLICATION_JSON).content(goodJsonData))
                .andReturn();
        TimelineResponseDto newTimeline
                = mapper.readValue(result.getResponse().getContentAsString(), TimelineResponseDto.class);

        requestUrlPath = TIMELINE_URL_PATH + "/" + newTimeline.getId();
    }

    @Test
    @DisplayName("(성공) 타임라인 업데이트: 해당 Id가 존재 + 모든 데이터 존재할 경우")
    public void isSuccessUpdateTimeline() throws Exception {
        String goodJsonData = "{\"title\": \"hoon\",\n\t\"body\": \"hoon\"}";
        MvcResult result = mockMvc.perform(put(requestUrlPath).contentType(MediaType.APPLICATION_JSON).content(goodJsonData))
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn();
        TimelineResponseDto newTimelineResponse
                = mapper.readValue(result.getResponse().getContentAsString(), TimelineResponseDto.class);

        assertThat(newTimelineResponse.getBody()).isEqualTo("hoon");
        assertThat(newTimelineResponse.getTitle()).isEqualTo("hoon");
    }

    @Test
    @DisplayName("(실패) 타임라인 업데이트: 존재하지 않는 id 업데이트를 한 경우")
    public void isFailDoNotExistTimelineId() throws Exception {
        String doNotExistTimeline = TIMELINE_URL_PATH + "/987654321";
        String goodJsonData = "{\"title\": \"new\",\n\t\"body\": \"new\"}";
        mockMvc.perform(put(doNotExistTimeline).contentType(MediaType.APPLICATION_JSON).content(goodJsonData))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("(실패) 타임라인 업데이트: Title 없을 때")
    public void isFailUpdateTimelineWithoutTitle() throws Exception {
        String jsonDataWithoutTitle = "{\"body\": \"hoon\"}";
        mockMvc.perform(put(requestUrlPath).contentType(MediaType.APPLICATION_JSON).content(jsonDataWithoutTitle))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("(실패) 타임라인 업데이트: Body 없을 때")
    public void isFailUpdateTimelineWithoutBody() throws Exception {
        String jsonDataWithoutBody = "{\"title\": \"new\"}";
        mockMvc.perform(put(requestUrlPath).contentType(MediaType.APPLICATION_JSON).content(jsonDataWithoutBody))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }
}
