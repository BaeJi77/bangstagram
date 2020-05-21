package com.bangstagram.timeline.controller;

import com.bangstagram.timeline.controller.dto.response.TimelineResponseDto;
import com.bangstagram.timeline.domain.repository.TimelineRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * author: Ji-Hoon Bae
 * Date: 2020.05.04
 */

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TimelineControllerGetTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TimelineRepository timelineRepository;

    @Autowired
    private ObjectMapper mapper;

    @BeforeEach
    public void setUp() throws Exception {
        timelineRepository.deleteAll();
        for (int i = 1; i <= 5 ; i++) {
            JSONObject jsonDummy = new JSONObject();
            jsonDummy.put("title", "testTitle");
            jsonDummy.put("body", "testBody");
            jsonDummy.put("roomId", 1);
            jsonDummy.put("userId", (long) i);
            mockMvc.perform(post("/timelines").contentType(MediaType.APPLICATION_JSON).content(jsonDummy.toString()));
        }
    }

    @Test
    @WithMockUser(roles = {"USER_ROLE"})
    @DisplayName("(성공) 타임라인 가져오기 (userId): userId에 해당하는 timeline array 획득")
    public void isSuccessFindAllTimelineRelatedUserId() throws Exception {
        for (int i = 1; i <= 5 ; i++) {
            MvcResult result = mockMvc.perform(get("/timelines" + "/" + i).contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andReturn();

            List<TimelineResponseDto> newTimelineList
                    = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<TimelineResponseDto>>() {});

            assertThat(newTimelineList.get(0).getTitle()).isEqualTo("testTitle");
            assertThat(newTimelineList.get(0).getBody()).isEqualTo("testBody");
            assertThat(newTimelineList.get(0).getRoomId()).isEqualTo(1L);
            assertThat(newTimelineList.get(0).getUserId()).isEqualTo((long) i);
        }
    }
}
