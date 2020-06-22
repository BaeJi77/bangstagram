package com.bangstagram.room.controller;

import com.bangstagram.room.controller.dto.request.ThemeSaveRequestDto;
import com.bangstagram.room.controller.dto.request.ThemeUpdateRequestDto;
import com.bangstagram.room.controller.dto.response.ThemeResponseDto;
import com.bangstagram.room.service.ThemeService;
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
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

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
class ThemeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ThemeService themeService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void findThemeById() throws Exception {
        // given
        ThemeResponseDto responseDto = ThemeResponseDto.builder()
                .id(1L)
                .roomId(1L)
                .title("theme")
                .description("desc")
                .imgSrc("src")
                .genre("genre")
                .build();
        given(themeService.findById(any(Long.class))).willReturn(responseDto);

        // when
        ResultActions result = mockMvc.perform(get("/rooms/{roomId}/themes/{themeId}", 1L, 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        byte[] contentAsByteArray = result.andDo(
                document("/rooms/theme-find",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("roomId").description("방탈출 id"),
                                parameterWithName("themeId").description("테마 id")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("테마 id"),
                                fieldWithPath("roomId").type(JsonFieldType.NUMBER).description("방탈출 id"),
                                fieldWithPath("title").type(JsonFieldType.STRING).description("테마 제목"),
                                fieldWithPath("imgSrc").type(JsonFieldType.STRING).description("테마 이미지"),
                                fieldWithPath("description").type(JsonFieldType.STRING).description("테마 설명"),
                                fieldWithPath("genre").type(JsonFieldType.STRING).description("장르")
                        )
                )
        )
                .andDo(print())
                .andReturn().getResponse().getContentAsByteArray();

        // then
        ThemeResponseDto themeResponseDto = objectMapper.readValue(contentAsByteArray, ThemeResponseDto.class);
        assertThat(themeResponseDto.getTitle()).isEqualTo(responseDto.getTitle());
    }

    @Test
    void findThemesByRoomId() throws Exception {
        // given
        List<ThemeResponseDto> themeResponseDtos = new ArrayList<>();
        IntStream.rangeClosed(1, 3).forEach(i -> themeResponseDtos.add(ThemeResponseDto.builder()
                .id(Long.parseLong(i + ""))
                .title("newTheme" + i)
                .roomId(1L)
                .genre("genre" + i)
                .imgSrc("src" + i)
                .description("desc" + i)
                .build()));
        given(themeService.findByRoomId(any(Long.class))).willReturn(themeResponseDtos);

        // when
        RequestBuilder requestBuilder;
        ResultActions result = mockMvc.perform(get("/rooms/{roomId}/themes", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        byte[] contentAsByteArray = result.andDo(document("/rooms/theme-findByRoomId",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(
                        parameterWithName("roomId").description("방탈출 id")
                ),
                responseFields(
                        fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("테마 id"),
                        fieldWithPath("[].roomId").type(JsonFieldType.NUMBER).description("방탈출 id"),
                        fieldWithPath("[].title").type(JsonFieldType.STRING).description("테마 제목"),
                        fieldWithPath("[].imgSrc").type(JsonFieldType.STRING).description("테마 이미지"),
                        fieldWithPath("[].description").type(JsonFieldType.STRING).description("테마 설명"),
                        fieldWithPath("[].genre").type(JsonFieldType.STRING).description("장르")
                )
        )).andDo(print())
                .andReturn().getResponse().getContentAsByteArray();

        // then
        ThemeResponseDto[] responseDtos = objectMapper.readValue(contentAsByteArray, ThemeResponseDto[].class);
        assertThat(responseDtos[0].getTitle()).isEqualTo(themeResponseDtos.get(0).getTitle());
        assertThat(responseDtos[0].getRoomId()).isEqualTo(themeResponseDtos.get(0).getRoomId());
        assertThat(responseDtos[0].getGenre()).isEqualTo(themeResponseDtos.get(0).getGenre());
        assertThat(responseDtos[0].getImgSrc()).isEqualTo(themeResponseDtos.get(0).getImgSrc());
        assertThat(responseDtos[0].getDescription()).isEqualTo(themeResponseDtos.get(0).getDescription());
    }

    @Test
    void createTheme() throws Exception {
        // given
        ThemeSaveRequestDto saveRequestDto = ThemeSaveRequestDto.builder()
                .title("newTheme")
                .roomId(1L)
                .imgSrc("src")
                .genre("genre")
                .description("desc")
                .build();
        given(themeService.createTheme(any(Long.class), any(ThemeSaveRequestDto.class))).willReturn(ThemeResponseDto.builder()
                .id(1L)
                .title(saveRequestDto.getTitle())
                .roomId(saveRequestDto.getRoomId())
                .genre(saveRequestDto.getGenre())
                .description(saveRequestDto.getDescription())
                .imgSrc(saveRequestDto.getImgSrc())
                .build());

        // when
        RequestBuilder requestBuilder;
        ResultActions result = mockMvc.perform(post("/rooms/{roomId}/themes", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(saveRequestDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        byte[] contentAsByteArray = result.andDo(document("/rooms/theme-create",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(
                        parameterWithName("roomId").description("방탈출 id")
                ),
                requestFields(
                        fieldWithPath("title").type(JsonFieldType.STRING).description("테마 제목"),
                        fieldWithPath("roomId").type(JsonFieldType.NUMBER).description("방탈출 id"),
                        fieldWithPath("imgSrc").type(JsonFieldType.STRING).description("테마 이미지"),
                        fieldWithPath("description").type(JsonFieldType.STRING).description("테마 설명"),
                        fieldWithPath("genre").type(JsonFieldType.STRING).description("테마 장르")
                ),
                responseFields(
                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("테마 id"),
                        fieldWithPath("roomId").type(JsonFieldType.NUMBER).description("방탈출 id"),
                        fieldWithPath("title").type(JsonFieldType.STRING).description("테마 제목"),
                        fieldWithPath("imgSrc").type(JsonFieldType.STRING).description("테마 이미지"),
                        fieldWithPath("description").type(JsonFieldType.STRING).description("테마 설명"),
                        fieldWithPath("genre").type(JsonFieldType.STRING).description("장르")
                )
        )).andDo(print())
                .andReturn().getResponse().getContentAsByteArray();

        // then
        ThemeResponseDto responseDto = objectMapper.readValue(contentAsByteArray, ThemeResponseDto.class);

        assertThat(responseDto.getTitle()).isEqualTo(saveRequestDto.getTitle());
        assertThat(responseDto.getRoomId()).isEqualTo(saveRequestDto.getRoomId());
        assertThat(responseDto.getDescription()).isEqualTo(saveRequestDto.getDescription());
        assertThat(responseDto.getImgSrc()).isEqualTo(saveRequestDto.getImgSrc());
        assertThat(responseDto.getGenre()).isEqualTo(saveRequestDto.getGenre());
    }

    @Test
    void updateTheme() throws Exception {
        // given
        ThemeUpdateRequestDto updateRequestDto = ThemeUpdateRequestDto.builder()
                .title("newTheme")
                .imgSrc("src")
                .genre("genre")
                .description("desc")
                .build();
        given(themeService.updateTheme(any(Long.class), any(Long.class), any(ThemeUpdateRequestDto.class))).willReturn(ThemeResponseDto.builder()
                .id(1L)
                .title(updateRequestDto.getTitle())
                .roomId(1L)
                .imgSrc(updateRequestDto.getImgSrc())
                .genre(updateRequestDto.getGenre())
                .description(updateRequestDto.getDescription())
                .build());

        // when
        RequestBuilder requestBuilder;
        ResultActions result = mockMvc.perform(put("/rooms/{roomId}/themes/{themeId}", 1L, 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequestDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        byte[] contentAsByteArray = result.andDo(document("/rooms/theme-update",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(
                        parameterWithName("roomId").description("방탈출 id"),
                        parameterWithName("themeId").description("테마 id")
                ),
                requestFields(
                        fieldWithPath("title").type(JsonFieldType.STRING).description("테마 제목"),
                        fieldWithPath("imgSrc").type(JsonFieldType.STRING).description("테마 이미지"),
                        fieldWithPath("description").type(JsonFieldType.STRING).description("테마 설명"),
                        fieldWithPath("genre").type(JsonFieldType.STRING).description("테마 장르")
                ),
                responseFields(
                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("테마 id"),
                        fieldWithPath("roomId").type(JsonFieldType.NUMBER).description("방탈출 id"),
                        fieldWithPath("title").type(JsonFieldType.STRING).description("테마 제목"),
                        fieldWithPath("imgSrc").type(JsonFieldType.STRING).description("테마 이미지"),
                        fieldWithPath("description").type(JsonFieldType.STRING).description("테마 설명"),
                        fieldWithPath("genre").type(JsonFieldType.STRING).description("장르")
                )
        )).andDo(print())
                .andReturn().getResponse().getContentAsByteArray();

        // then
        ThemeResponseDto responseDto = objectMapper.readValue(contentAsByteArray, ThemeResponseDto.class);

        assertThat(responseDto.getTitle()).isEqualTo(updateRequestDto.getTitle());
        assertThat(responseDto.getGenre()).isEqualTo(updateRequestDto.getGenre());
        assertThat(responseDto.getImgSrc()).isEqualTo(updateRequestDto.getImgSrc());
        assertThat(responseDto.getDescription()).isEqualTo(updateRequestDto.getDescription());
    }
}