package com.bangstagram.room.controller;

import com.bangstagram.room.controller.dto.response.RoomResponseDto;
import com.bangstagram.room.service.RoomService;
import com.bangstagram.user.configure.security.WebSecurityConfigure;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = RoomController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigure.class)
        })
public class RoomControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RoomService roomService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("방탈출 리스트 조회 테스트")
    void findAllRooms() throws Exception {
        RoomResponseDto room = RoomResponseDto.builder()
                .title("room_title")
                .link("link")
                .phone("phone")
                .address("addr")
                .description("desc").build();
        given(roomService.findAll()).willReturn(Collections.singletonList(room));

        mockMvc.perform(get("/rooms"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", iterableWithSize(1)))
                .andExpect(jsonPath("$[0]['title']", is(equalTo("room_title"))))
                .andDo(print());
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("방탈출 ID 조회 테스트")
    void findById() throws Exception {
        //given
        RoomResponseDto room = RoomResponseDto.builder()
                .id(1L)
                .title("room_title")
                .link("link")
                .phone("phone")
                .address("addr")
                .description("desc")
                .build();
        given(roomService.findById(room.getId())).willReturn(room);

        //when
        byte[] contentAsByteArray = mockMvc.perform(get("/rooms/{id}", room.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andReturn().getResponse().getContentAsByteArray();


        //then
        RoomResponseDto response = objectMapper.readValue(contentAsByteArray, RoomResponseDto.class);
        assertThat(response.getId()).isEqualTo(room.getId());
        assertThat(response.getTitle()).isEqualTo(room.getTitle());
        assertThat(response.getAddress()).isEqualTo(room.getAddress());
        assertThat(response.getLink()).isEqualTo(room.getLink());
        assertThat(response.getPhone()).isEqualTo(room.getPhone());
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("방탈출 정보 지역별 검색")
    void searchRoomByRegion() throws Exception {
        RoomResponseDto room = RoomResponseDto.builder().title("room_title").address("서울 서초구 서초대로77길").build();
        given(roomService.findRoomByRegion("서초")).willReturn(Collections.singletonList(room));

        mockMvc.perform(get("/rooms/search").param("region", "서초"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0]['title']", is(equalTo("room_title"))))
                .andDo(print());
    }
}
