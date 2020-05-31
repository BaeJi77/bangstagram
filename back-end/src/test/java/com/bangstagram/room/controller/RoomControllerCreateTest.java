package com.bangstagram.room.controller;

import com.bangstagram.room.controller.dto.request.RoomSaveRequestDto;
import com.bangstagram.room.controller.dto.response.RoomResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
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
    @WithMockUser(roles = "USER")
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
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        RoomResponseDto responseDto = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), RoomResponseDto.class);
        assertThat(responseDto.getTitle()).isEqualTo(saveRequestDto.getTitle());
        assertThat(responseDto.getAddress()).isEqualTo(saveRequestDto.getAddress());
        assertThat(responseDto.getPhone()).isEqualTo(saveRequestDto.getPhone());
        assertThat(responseDto.getLink()).isEqualTo(saveRequestDto.getLink());
        assertThat(responseDto.getDescription()).isEqualTo(saveRequestDto.getDescription());
    }

}
