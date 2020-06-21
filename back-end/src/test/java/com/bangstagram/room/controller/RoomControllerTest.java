package com.bangstagram.room.controller;

import com.bangstagram.room.controller.dto.request.RoomSaveRequestDto;
import com.bangstagram.room.controller.dto.request.RoomUpdateRequestDto;
import com.bangstagram.room.controller.dto.response.RoomResponseDto;
import com.bangstagram.room.service.RoomService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Collections;
import java.util.List;

import static com.bangstagram.common.ApiDocumentUtils.getDocumentRequest;
import static com.bangstagram.common.ApiDocumentUtils.getDocumentResponse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/*@WebMvcTest(controllers = RoomController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigure.class)
        })*/
@AutoConfigureRestDocs
@AutoConfigureMockMvc
@SpringBootTest
public class RoomControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RoomService roomService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("방탈출 리스트 조회 테스트")
    void findAllRooms() throws Exception {
        // given
        List<RoomResponseDto> responseDtos = Collections.singletonList(RoomResponseDto.builder()
                .id(1L)
                .title("newRoom")
                .link("link")
                .phone("phone")
                .address("addr")
                .description("desc").build());
        given(roomService.findAll()).willReturn(responseDtos);

        // when
        ResultActions result = mockMvc.perform(get("/rooms"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        byte[] contentAsByteArray = result.andDo(document("rooms/rooms-find",
                getDocumentRequest(),
                getDocumentResponse(),
                responseFields(
                        fieldWithPath("[]").description("방탈출 list"),
                        fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("방탈출 id"),
                        fieldWithPath("[].title").type(JsonFieldType.STRING).description("방탈출 제목"),
                        fieldWithPath("[].address").type(JsonFieldType.STRING).description("방탈출 주소"),
                        fieldWithPath("[].link").type(JsonFieldType.STRING).description("방탈출 링크"),
                        fieldWithPath("[].phone").type(JsonFieldType.STRING).description("방탈출 전화번호"),
                        fieldWithPath("[].description").type(JsonFieldType.STRING).description("방탈출 상세설명")
                )
                )
        ).andDo(print())
                .andReturn().getResponse().getContentAsByteArray();

        // then
        RoomResponseDto[] roomResponseDtos = objectMapper.readValue(contentAsByteArray, RoomResponseDto[].class);
        assertThat(roomResponseDtos[0].getTitle()).isEqualTo(responseDtos.get(0).getTitle());
        assertThat(roomResponseDtos[0].getAddress()).isEqualTo(responseDtos.get(0).getAddress());
        assertThat(roomResponseDtos[0].getPhone()).isEqualTo(responseDtos.get(0).getPhone());
        assertThat(roomResponseDtos[0].getLink()).isEqualTo(responseDtos.get(0).getLink());
        assertThat(roomResponseDtos[0].getDescription()).isEqualTo(responseDtos.get(0).getDescription());
    }

    @Test
    @DisplayName("방탈출 ID 조회 테스트")
    void findById() throws Exception {
        //given
        RoomResponseDto responseDto = RoomResponseDto.builder()
                .id(1L)
                .title("room_title")
                .link("link")
                .phone("phone")
                .address("addr")
                .description("desc")
                .build();
        given(roomService.findById(any(Long.class))).willReturn(responseDto);

        //when
        ResultActions result = mockMvc.perform(RestDocumentationRequestBuilders.get("/rooms/{id}", any(Long.class)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        byte[] contentAsByteArray = result.andDo(document("/rooms/rooms-findById",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(
                        parameterWithName("id").description("방탈출 id")
                ),
                responseFields(
                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("방탈출 id"),
                        fieldWithPath("title").type(JsonFieldType.STRING).description("방탈출 제목"),
                        fieldWithPath("address").type(JsonFieldType.STRING).description("방탈출 주소"),
                        fieldWithPath("link").type(JsonFieldType.STRING).description("방탈출 링크"),
                        fieldWithPath("phone").type(JsonFieldType.STRING).description("방탈출 전화번호"),
                        fieldWithPath("description").type(JsonFieldType.STRING).description("방탈출 상세설명")
                )
        )).andDo(print())
                .andReturn().getResponse().getContentAsByteArray();

        //then
        RoomResponseDto roomResponseDto = objectMapper.readValue(contentAsByteArray, RoomResponseDto.class);
        assertThat(roomResponseDto.getTitle()).isEqualTo(responseDto.getTitle());
        assertThat(roomResponseDto.getAddress()).isEqualTo(responseDto.getAddress());
        assertThat(roomResponseDto.getPhone()).isEqualTo(responseDto.getPhone());
        assertThat(roomResponseDto.getLink()).isEqualTo(responseDto.getLink());
        assertThat(roomResponseDto.getDescription()).isEqualTo(responseDto.getDescription());
    }

    @Test
    @DisplayName("방탈출 정보 지역별 검색")
    void searchRoomByRegion() throws Exception {
        // given
        List<RoomResponseDto> roomResponseDtos = Collections.singletonList(RoomResponseDto.builder()
                .id(1L)
                .title("room title")
                .link("link")
                .phone("phone")
                .address("room address")
                .description("desc")
                .build());

        given(roomService.findRoomByRegion(any(String.class))).willReturn(roomResponseDtos);

        // when
        ResultActions result = mockMvc.perform(get("/rooms/search")
                .param("region", "address"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        byte[] contentAsByteArray = result.andDo(document("rooms/rooms-findByRegion",
                getDocumentRequest(),
                getDocumentResponse(),
                requestParameters(
                        parameterWithName("region").description("방탈출 지역")
                ),
                responseFields(
                        fieldWithPath("[]").description("방탈출 list"),
                        fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("방탈출 id"),
                        fieldWithPath("[].title").type(JsonFieldType.STRING).description("방탈출 제목"),
                        fieldWithPath("[].address").type(JsonFieldType.STRING).description("방탈출 주소"),
                        fieldWithPath("[].link").type(JsonFieldType.STRING).description("방탈출 링크"),
                        fieldWithPath("[].phone").type(JsonFieldType.STRING).description("방탈출 전화번호"),
                        fieldWithPath("[].description").type(JsonFieldType.STRING).description("방탈출 상세설명")
                )

        )).andDo(print())
                .andReturn().getResponse().getContentAsByteArray();

        // then
        RoomResponseDto[] responseDtos = objectMapper.readValue(contentAsByteArray, RoomResponseDto[].class);
        assertThat(responseDtos[0].getTitle()).isEqualTo(roomResponseDtos.get(0).getTitle());
        assertThat(responseDtos[0].getAddress()).isEqualTo(roomResponseDtos.get(0).getAddress());
        assertThat(responseDtos[0].getPhone()).isEqualTo(roomResponseDtos.get(0).getPhone());
        assertThat(responseDtos[0].getLink()).isEqualTo(roomResponseDtos.get(0).getLink());
        assertThat(responseDtos[0].getDescription()).isEqualTo(roomResponseDtos.get(0).getDescription());
    }

    @Test
    @DisplayName("방탈출 정보 생성")
    void createRoom() throws Exception {
        // given
        RoomSaveRequestDto saveRequestDto = RoomSaveRequestDto.builder()
                .title("room title")
                .address("address")
                .phone("phone")
                .link("link")
                .description("desc")
                .build();
        given(roomService.createRoom(any(RoomSaveRequestDto.class))).willReturn(RoomResponseDto.builder()
                .id(1L)
                .title(saveRequestDto.getTitle())
                .address(saveRequestDto.getAddress())
                .phone(saveRequestDto.getPhone())
                .link(saveRequestDto.getLink())
                .description(saveRequestDto.getDescription())
                .build());

        // when
        ResultActions result = mockMvc.perform(post("/rooms")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(saveRequestDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        byte[] contentAsByteArray = result.andDo(document("/rooms/room-create",
                getDocumentRequest(),
                getDocumentResponse(),
                requestFields(
                        fieldWithPath("title").type(JsonFieldType.STRING).description("방탈출 제목"),
                        fieldWithPath("address").type(JsonFieldType.STRING).description("방탈출 주소"),
                        fieldWithPath("link").type(JsonFieldType.STRING).description("방탈출 링크"),
                        fieldWithPath("phone").type(JsonFieldType.STRING).description("방탈출 전화번호"),
                        fieldWithPath("description").type(JsonFieldType.STRING).description("방탈출 상세설명")
                ),
                responseFields(
                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("방탈출 id"),
                        fieldWithPath("title").type(JsonFieldType.STRING).description("방탈출 제목"),
                        fieldWithPath("address").type(JsonFieldType.STRING).description("방탈출 주소"),
                        fieldWithPath("link").type(JsonFieldType.STRING).description("방탈출 링크"),
                        fieldWithPath("phone").type(JsonFieldType.STRING).description("방탈출 전화번호"),
                        fieldWithPath("description").type(JsonFieldType.STRING).description("방탈출 상세설명")
                )
        ))
                .andDo(print())
                .andReturn().getResponse().getContentAsByteArray();

        // then
        RoomResponseDto responseDto = objectMapper.readValue(contentAsByteArray, RoomResponseDto.class);
        assertThat(responseDto.getTitle()).isEqualTo(saveRequestDto.getTitle());
        assertThat(responseDto.getAddress()).isEqualTo(saveRequestDto.getAddress());
        assertThat(responseDto.getPhone()).isEqualTo(saveRequestDto.getPhone());
        assertThat(responseDto.getLink()).isEqualTo(saveRequestDto.getLink());
        assertThat(responseDto.getDescription()).isEqualTo(saveRequestDto.getDescription());
    }

    @Test
    @DisplayName("방탈출 정보 수정")
    void updateRoom() throws Exception {
        // given
        RoomUpdateRequestDto updateRequestDto = RoomUpdateRequestDto.builder()
                .title("room title")
                .address("address")
                .phone("phone")
                .link("link")
                .description("desc")
                .build();
        given(roomService.updateRoom(any(Long.class), any(RoomUpdateRequestDto.class))).willReturn(RoomResponseDto.builder()
                .id(1L)
                .title(updateRequestDto.getTitle())
                .address(updateRequestDto.getAddress())
                .phone(updateRequestDto.getPhone())
                .link(updateRequestDto.getLink())
                .description(updateRequestDto.getDescription())
                .build());

        // when
        ResultActions result = mockMvc.perform(RestDocumentationRequestBuilders.put("/rooms/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequestDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        byte[] contentAsByteArray = result.andDo(document("/rooms/room-update",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(
                        parameterWithName("id").description("방탈출 id")
                ),
                requestFields(
                        fieldWithPath("title").type(JsonFieldType.STRING).description("방탈출 제목"),
                        fieldWithPath("address").type(JsonFieldType.STRING).description("방탈출 주소"),
                        fieldWithPath("link").type(JsonFieldType.STRING).description("방탈출 링크"),
                        fieldWithPath("phone").type(JsonFieldType.STRING).description("방탈출 전화번호"),
                        fieldWithPath("description").type(JsonFieldType.STRING).description("방탈출 상세설명")
                ),
                responseFields(
                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("방탈출 id"),
                        fieldWithPath("title").type(JsonFieldType.STRING).description("방탈출 제목"),
                        fieldWithPath("address").type(JsonFieldType.STRING).description("방탈출 주소"),
                        fieldWithPath("link").type(JsonFieldType.STRING).description("방탈출 링크"),
                        fieldWithPath("phone").type(JsonFieldType.STRING).description("방탈출 전화번호"),
                        fieldWithPath("description").type(JsonFieldType.STRING).description("방탈출 상세설명")
                )
        ))
                .andDo(print())
                .andReturn().getResponse().getContentAsByteArray();

        // then
        RoomResponseDto responseDto = objectMapper.readValue(contentAsByteArray, RoomResponseDto.class);
        assertThat(responseDto.getTitle()).isEqualTo(updateRequestDto.getTitle());
        assertThat(responseDto.getAddress()).isEqualTo(updateRequestDto.getAddress());
        assertThat(responseDto.getPhone()).isEqualTo(updateRequestDto.getPhone());
        assertThat(responseDto.getLink()).isEqualTo(updateRequestDto.getLink());
        assertThat(responseDto.getDescription()).isEqualTo(updateRequestDto.getDescription());
    }
}
