package com.bangstagram.room.controller;

import com.bangstagram.room.controller.dto.request.ReviewSaveRequestDto;
import com.bangstagram.room.controller.dto.request.ReviewUpdateRequestDto;
import com.bangstagram.room.controller.dto.response.ReviewResponseDto;
import com.bangstagram.room.service.ReviewService;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.util.Collections;

import static com.bangstagram.common.ApiDocumentUtils.getDocumentRequest;
import static com.bangstagram.common.ApiDocumentUtils.getDocumentResponse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureRestDocs
@AutoConfigureMockMvc
@SpringBootTest
class ReviewControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReviewService reviewService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createReviews() throws Exception {
        // given
        ReviewSaveRequestDto saveRequestDto = ReviewSaveRequestDto.builder()
                .userId(1L)
                .themeId(1L)
                .score(5)
                .success(true)
                .level("어려움")
                .content("newReview")
                .hintCount(3)
                .leftTime(60)
                .build();
        given(reviewService.createReview(any(Long.class), any(Long.class), any(ReviewSaveRequestDto.class)))
                .willReturn(ReviewResponseDto.builder()
                        .id(1L)
                        .userId(saveRequestDto.getUserId())
                        .themeId(saveRequestDto.getThemeId())
                        .score(saveRequestDto.getScore())
                        .level(saveRequestDto.getLevel())
                        .hintCount(saveRequestDto.getHintCount())
                        .success(saveRequestDto.isSuccess())
                        .content(saveRequestDto.getContent())
                        .leftTime(saveRequestDto.getLeftTime())
                        .build());

        // when
        ResultActions result = mockMvc.perform(post("/rooms/{roomId}/themes/{themeId}/reviews", 1L, 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(saveRequestDto)));

        byte[] contentAsByteArray = result
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(document("rooms/review-create",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("roomId").description("방탈출 id"),
                                parameterWithName("themeId").description("테마 id")
                        ),
                        requestFields(
                                fieldWithPath("userId").type(JsonFieldType.NUMBER).description("사용자 id"),
                                fieldWithPath("themeId").type(JsonFieldType.NUMBER).description("테마 id"),
                                fieldWithPath("score").type(JsonFieldType.NUMBER).description("별점"),
                                fieldWithPath("level").type(JsonFieldType.STRING).description("난이도"),
                                fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공/실패"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("리뷰내용"),
                                fieldWithPath("leftTime").type(JsonFieldType.NUMBER).description("남은 시간"),
                                fieldWithPath("hintCount").type(JsonFieldType.NUMBER).description("힌트 사용")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("리뷰 id"),
                                fieldWithPath("userId").type(JsonFieldType.NUMBER).description("사용자 id"),
                                fieldWithPath("themeId").type(JsonFieldType.NUMBER).description("테마 id"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("리뷰내용"),
                                fieldWithPath("score").type(JsonFieldType.NUMBER).description("별점"),
                                fieldWithPath("level").type(JsonFieldType.STRING).description("난이도"),
                                fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공/실패"),
                                fieldWithPath("leftTime").type(JsonFieldType.NUMBER).description("남은 시간"),
                                fieldWithPath("hintCount").type(JsonFieldType.NUMBER).description("힌트 사용")
                        )
                ))
                .andDo(print())
                .andReturn().getResponse().getContentAsByteArray();

        // then
        ReviewResponseDto reviewResponseDto = objectMapper.readValue(contentAsByteArray, ReviewResponseDto.class);

        assertThat(reviewResponseDto.getContent()).isEqualTo(saveRequestDto.getContent());
        assertThat(reviewResponseDto.getScore()).isEqualTo(saveRequestDto.getScore());
        assertThat(reviewResponseDto.getHintCount()).isEqualTo(saveRequestDto.getHintCount());
        assertThat(reviewResponseDto.getLeftTime()).isEqualTo(saveRequestDto.getLeftTime());
        assertThat(reviewResponseDto.getLevel()).isEqualTo(saveRequestDto.getLevel());
        assertThat(reviewResponseDto.getThemeId()).isEqualTo(saveRequestDto.getThemeId());
        assertThat(reviewResponseDto.getUserId()).isEqualTo(saveRequestDto.getUserId());
    }

    @Test
    void findReviewsByThemeId() throws Exception {
        // given
        ReviewResponseDto reviewResponseDto = ReviewResponseDto.builder()
                .id(1L)
                .userId(1L)
                .themeId(1L)
                .content("review")
                .hintCount(1)
                .leftTime(10)
                .score(100)
                .success(true)
                .level("level")
                .build();
        given(reviewService.findByThemeId(any(Long.class), any(Long.class))).willReturn(Collections.singletonList(reviewResponseDto));

        // when
        ResultActions result = mockMvc.perform(get("/rooms/{roomId}/themes/{themeId}/reviews", 1L, 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        byte[] contentAsByteArray = result
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(document("rooms/review-findByThemeId",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("roomId").description("방탈출 id"),
                                parameterWithName("themeId").description("테마 id")
                        ),
                        responseFields(
                                fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("리뷰 id"),
                                fieldWithPath("[].userId").type(JsonFieldType.NUMBER).description("사용자 id"),
                                fieldWithPath("[].themeId").type(JsonFieldType.NUMBER).description("테마 id"),
                                fieldWithPath("[].content").type(JsonFieldType.STRING).description("리뷰내용"),
                                fieldWithPath("[].score").type(JsonFieldType.NUMBER).description("별점"),
                                fieldWithPath("[].level").type(JsonFieldType.STRING).description("난이도"),
                                fieldWithPath("[].success").type(JsonFieldType.BOOLEAN).description("성공/실패"),
                                fieldWithPath("[].leftTime").type(JsonFieldType.NUMBER).description("남은 시간"),
                                fieldWithPath("[].hintCount").type(JsonFieldType.NUMBER).description("힌트 사용")
                        )
                ))
                .andDo(print())
                .andReturn().getResponse().getContentAsByteArray();

        // then
        ReviewResponseDto[] responseDtos = objectMapper.readValue(contentAsByteArray, ReviewResponseDto[].class);

        assertThat(responseDtos[0].getContent()).isEqualTo(reviewResponseDto.getContent());
        assertThat(responseDtos[0].getUserId()).isEqualTo(reviewResponseDto.getUserId());
        assertThat(responseDtos[0].getThemeId()).isEqualTo(reviewResponseDto.getThemeId());
        assertThat(responseDtos[0].getLevel()).isEqualTo(reviewResponseDto.getLevel());
        assertThat(responseDtos[0].getLeftTime()).isEqualTo(reviewResponseDto.getLeftTime());
        assertThat(responseDtos[0].getHintCount()).isEqualTo(reviewResponseDto.getHintCount());
        assertThat(responseDtos[0].getScore()).isEqualTo(reviewResponseDto.getScore());
        assertThat(responseDtos[0].isSuccess()).isEqualTo(reviewResponseDto.isSuccess());
    }

    @Test
    void updateReview() throws Exception {
        // given
        ReviewUpdateRequestDto updateRequestDto = ReviewUpdateRequestDto.builder()
                .score(5)
                .success(true)
                .level("어려움")
                .content("newReview")
                .hintCount(3)
                .leftTime(60)
                .build();
        given(reviewService.updateReview(any(Long.class), any(Long.class), any(Long.class), any(ReviewUpdateRequestDto.class)))
                .willReturn(ReviewResponseDto.builder()
                        .id(1L)
                        .userId(1L)
                        .themeId(1L)
                        .score(updateRequestDto.getScore())
                        .level(updateRequestDto.getLevel())
                        .hintCount(updateRequestDto.getHintCount())
                        .success(updateRequestDto.isSuccess())
                        .content(updateRequestDto.getContent())
                        .leftTime(updateRequestDto.getLeftTime())
                        .build());

        // when
        ResultActions result = mockMvc.perform(put("/rooms/{roomId}/themes/{themeId}/reviews/{reviewId}", 1L, 1L, 1L)
                .content(objectMapper.writeValueAsString(updateRequestDto))
                .contentType(MediaType.APPLICATION_JSON));

        byte[] contentAsByteArray = result
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(document("rooms/review-update",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("roomId").description("방탈출 id"),
                                parameterWithName("themeId").description("테마 id"),
                                parameterWithName("reviewId").description("리뷰 id")
                        ),
                        requestFields(
                                fieldWithPath("score").type(JsonFieldType.NUMBER).description("별점"),
                                fieldWithPath("level").type(JsonFieldType.STRING).description("난이도"),
                                fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공/실패"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("리뷰내용"),
                                fieldWithPath("leftTime").type(JsonFieldType.NUMBER).description("남은 시간"),
                                fieldWithPath("hintCount").type(JsonFieldType.NUMBER).description("힌트 사용")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("리뷰 id"),
                                fieldWithPath("userId").type(JsonFieldType.NUMBER).description("사용자 id"),
                                fieldWithPath("themeId").type(JsonFieldType.NUMBER).description("테마 id"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("리뷰내용"),
                                fieldWithPath("score").type(JsonFieldType.NUMBER).description("별점"),
                                fieldWithPath("level").type(JsonFieldType.STRING).description("난이도"),
                                fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공/실패"),
                                fieldWithPath("leftTime").type(JsonFieldType.NUMBER).description("남은 시간"),
                                fieldWithPath("hintCount").type(JsonFieldType.NUMBER).description("힌트 사용")
                        )
                ))
                .andDo(print())
                .andReturn().getResponse().getContentAsByteArray();

        // them
        ReviewResponseDto responseDto = objectMapper.readValue(contentAsByteArray, ReviewResponseDto.class);
        assertThat(responseDto.getContent()).isEqualTo(updateRequestDto.getContent());
        assertThat(responseDto.getLevel()).isEqualTo(updateRequestDto.getLevel());
        assertThat(responseDto.getLeftTime()).isEqualTo(updateRequestDto.getLeftTime());
        assertThat(responseDto.getScore()).isEqualTo(updateRequestDto.getScore());
        assertThat(responseDto.getHintCount()).isEqualTo(updateRequestDto.getHintCount());
    }

    @Test
    void deleteReview() throws Exception {
        mockMvc.perform(delete("/rooms/{roomId}/themes/{themeId}/reviews/{reviewId}", 1L, 1L, 1L))
                .andExpect(status().isOk())
                .andDo(document("rooms/review-delete",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("roomId").description("방탈출 id"),
                                parameterWithName("themeId").description("테마 id"),
                                parameterWithName("reviewId").description("리뷰 id")
                        ))
                )
                .andDo(print());
    }
}