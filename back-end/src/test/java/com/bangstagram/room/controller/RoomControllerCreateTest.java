package com.bangstagram.room.controller;

import com.bangstagram.room.controller.dto.RoomResponseDto;
import com.bangstagram.room.controller.dto.RoomSaveRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.BDDAssertions.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
public class RoomControllerCreateTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("방탈출 정보 생성")
    void createRoom() throws Exception {
        RoomSaveRequestDto saveRequestDto = RoomSaveRequestDto.builder()
                .title("test_title")
                .address("test_addr")
                .phone("test_phone")
                .link("test_link")
                .description("test_desc")
                .build();
        MvcResult mvcResult = mockMvc.perform(post("/rooms")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(saveRequestDto)))
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn();

        RoomResponseDto responseDto = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), RoomResponseDto.class);
        then(responseDto.getTitle()).isEqualTo(saveRequestDto.getTitle());
        then(responseDto.getAddress()).isEqualTo(saveRequestDto.getAddress());
        then(responseDto.getPhone()).isEqualTo(saveRequestDto.getPhone());
        then(responseDto.getLink()).isEqualTo(saveRequestDto.getLink());
        then(responseDto.getDescription()).isEqualTo(saveRequestDto.getDescription());
    }

}
