package com.bangstagram.room.controller;

import com.bangstagram.room.controller.dto.RoomResponseDto;
import com.bangstagram.room.service.RoomService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RoomController.class)
public class RoomControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RoomService roomService;

    @Test
    public void room_getAll_test() throws Exception {
        RoomResponseDto room = RoomResponseDto.builder()
                .title("room_title")
                .link("link")
                .phone("phone")
                .address("addr")
                .description("desc").build();
        given(roomService.getAll()).willReturn(Collections.singletonList(room));

        mockMvc.perform(get("/rooms"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", iterableWithSize(1)))
                .andExpect(jsonPath("$[0]['title']", is(equalTo("room_title"))));

    }
}
