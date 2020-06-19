package com.bangstagram.timeline.controller;

import com.bangstagram.timeline.controller.dto.request.TimelineCommentRequestDto;
import com.bangstagram.timeline.controller.dto.response.TimelineCommentResponseDto;
import com.bangstagram.timeline.domain.model.Timeline;
import com.bangstagram.timeline.domain.repository.TimelineCommentRepository;
import com.bangstagram.timeline.domain.repository.TimelineRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/***
 * author: Ji-Hoon, Bae
 * date: 2020.06.10
 */

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TimelineCommentControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TimelineRepository timelineRepository;

    @Autowired
    private TimelineCommentRepository timelineCommentRepository;

//    @MockBean
//    private TimelineCommentService timelineCommentService;

    @BeforeEach
    void setUp() {
        timelineRepository.deleteAll();
        timelineCommentRepository.deleteAll();
    }

    // 성
    @Test
    @WithMockUser
    @DisplayName("타임라인 코멘트 만들기: 모든 정보가 정상일 때")
    void createNewTimelineComment() throws Exception {
        //given
//        given(timelineCommentService.makeTimelineComment(any(TimelineCommentRequestDto.class))).willReturn(response)

        Timeline timeline = Timeline.builder()
                .title("new")
                .body("new")
                .roomId(1L)
                .userId(1L)
                .build();

        timelineRepository.save(timeline);

        TimelineCommentRequestDto testDto = new TimelineCommentRequestDto("newComment", 1L, 1L);

        byte[] contentAsByteArray = mockMvc.perform(post("/timelines/comments")
                .content(objectMapper.writeValueAsString(testDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsByteArray();

        TimelineCommentResponseDto response = objectMapper.readValue(contentAsByteArray, TimelineCommentResponseDto.class);

        //then
        assertThat(response.getComment()).isEqualTo(testDto.getComment());
        assertThat(response.getUserId()).isEqualTo(testDto.getUserId());
        assertThat(response.getTimelineId()).isEqualTo(testDto.getTimelineId());
    }


    // 데이터 발리데이션
    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("타임라인 코멘트 만들기 실패: userId가 없을 때")
    void createNewTimelineCommentTest1() throws Exception {
        TimelineCommentRequestDto testDto = new TimelineCommentRequestDto("newComment", null, 1L);

        mockMvc.perform(post("/timelines/comments")
                .content(objectMapper.writeValueAsString(testDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("타임라인 코멘트 만들기 실패: timelineId 없을 때")
    void createNewTimelineCommentTest2() throws Exception {
        TimelineCommentRequestDto testDto = new TimelineCommentRequestDto("newComment", 1L, null);

        mockMvc.perform(post("/timelines/comments")
                .content(objectMapper.writeValueAsString(testDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("타임라인 코멘트 만들기 실패: comment가 없을 때")
    void createNewTimelineCommentTest3() throws Exception {
        TimelineCommentRequestDto testDto = new TimelineCommentRequestDto(null, 1L, 1L);

        mockMvc.perform(post("/timelines/comments")
                .content(objectMapper.writeValueAsString(testDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}