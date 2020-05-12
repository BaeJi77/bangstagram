package com.bangstagram.room.controller;

import com.bangstagram.room.controller.dto.RoomResponseDto;
import com.bangstagram.room.service.RoomService;
import com.bangstagram.user.configure.WebSecurityConfigure;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

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

    @Test
    @WithMockUser(roles = "USER_ROLE")
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
    @WithMockUser(roles = "USER_ROLE")
    @DisplayName("방탈출 ID 조회 테스트")
    void findById() throws Exception {
        RoomResponseDto room = RoomResponseDto.builder()
                .id(1L)
                .title("room_title")
                .link("link")
                .phone("phone")
                .address("addr")
                .description("desc")
                .build();
        given(roomService.findById(room.getId())).willReturn(room);

        mockMvc.perform(get("/rooms/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title", is(equalTo("room_title"))))
                .andDo(print());
    }

    @Test
    @WithMockUser(roles = "USER_ROLE")
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
