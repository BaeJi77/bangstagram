package com.bangstagram.timeline.controller;

import com.bangstagram.timeline.controller.dto.request.TimelineCommentRequestDto;
import com.bangstagram.timeline.controller.dto.request.TimelineCommentUpdateRequestDto;
import com.bangstagram.timeline.controller.dto.response.TimelineCommentResponseDto;
import com.bangstagram.timeline.domain.repository.TimelineCommentRepository;
import com.bangstagram.timeline.domain.repository.TimelineRepository;
import com.bangstagram.timeline.service.TimelineCommentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/***
 * author: Ji-Hoon, Bae
 * date: 2020.06.10
 */

@AutoConfigureMockMvc
@AutoConfigureRestDocs
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

    @MockBean
    private TimelineCommentService timelineCommentService;

    @BeforeEach
    void setUp() {
        timelineRepository.deleteAll();
        timelineCommentRepository.deleteAll();
    }

    @Test
    @WithMockUser(roles = {"USER"})
    @DisplayName("타임라인 코멘트 만들기: 모든 정보가 정상일 때")
    void createNewTimelineComment() throws Exception {
        //given
        TimelineCommentRequestDto testDto = new TimelineCommentRequestDto("newComment", 1L, 1L);
        TimelineCommentResponseDto timelineCommentResponseDto = TimelineCommentResponseDto.builder()
                .id(1L)
                .timelineId(1L)
                .userId(1L)
                .comment("newComment")
                .build();

        given(timelineCommentService.makeTimelineComment(any(TimelineCommentRequestDto.class))).willReturn(timelineCommentResponseDto);

        // when
        byte[] contentAsByteArray = mockMvc.perform(post("/timelines/comments")
                .content(objectMapper.writeValueAsString(testDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("timelines/comments/create",
                        responseFields(
                                fieldWithPath("id")
                                        .description("만들어진 타임라인 코멘트 ID"),
                                fieldWithPath("comment")
                                        .description("사용자가 만든 코멘트"),
                                fieldWithPath("userId")
                                        .description("만든 User ID"),
                                fieldWithPath("timelineId")
                                        .description("연결되어있는 Timeline ID")
                        )
                ))
                .andReturn().getResponse().getContentAsByteArray();

        TimelineCommentResponseDto response = objectMapper.readValue(contentAsByteArray, TimelineCommentResponseDto.class);

        //then
        assertThat(response.getComment()).isEqualTo(testDto.getComment());
        assertThat(response.getUserId()).isEqualTo(testDto.getUserId());
        assertThat(response.getTimelineId()).isEqualTo(testDto.getTimelineId());
    }


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

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("타임라인 코멘트 업데이트 성공")
    void updateTimelineComment() throws Exception {
        // given
        TimelineCommentUpdateRequestDto timelineCommentUpdateRequestDto = new TimelineCommentUpdateRequestDto("UpdateComment");

        TimelineCommentResponseDto responseDto = TimelineCommentResponseDto.builder()
                .id(1L)
                .comment("UpdateComment")
                .userId(1L)
                .timelineId(1L)
                .build();

        given(timelineCommentService.updateComment(any(Long.class), any(TimelineCommentUpdateRequestDto.class)))
                .willReturn(responseDto);

        // when
        String contentAsString = mockMvc.perform(patch("/timelines/comments/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(timelineCommentUpdateRequestDto)))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("timelines/comments/update",
                        responseFields(
                                fieldWithPath("id")
                                        .description("업데이트된 타임라인 코멘트 ID"),
                                fieldWithPath("comment")
                                        .description("업데이트되어진 코멘트 결과"),
                                fieldWithPath("userId")
                                        .description("만든 User ID"),
                                fieldWithPath("timelineId")
                                        .description("연결되어있는 Timeline ID")
                        )
                ))
                .andReturn().getResponse().getContentAsString();

        TimelineCommentResponseDto resultDto = objectMapper.readValue(contentAsString, TimelineCommentResponseDto.class);

        //then
        assertThat(resultDto.getComment()).isEqualTo("UpdateComment");
        assertThat(resultDto.getTimelineId()).isEqualTo(1L);
        assertThat(resultDto.getUserId()).isEqualTo(1L);
    }

    @Test
    @WithMockUser
    @DisplayName("타임라인 업데이트: (실패) 코멘트가 없는 경우")
    void updateTimelineCommentWithoutComment() throws Exception {
        mockMvc.perform(patch("/timelines/comments/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new TimelineCommentUpdateRequestDto())))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }
}