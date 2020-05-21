package com.bangstagram.room.controller;

import com.bangstagram.room.controller.dto.request.RoomSaveRequestDto;
import com.bangstagram.room.controller.dto.request.RoomUpdateRequestDto;
import com.bangstagram.room.controller.dto.response.RoomResponseDto;
import com.bangstagram.room.domain.repository.RoomRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
public class RoomControllerUpdateTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RoomRepository roomRepository;

    private Long id;

    @BeforeEach
    void setup() throws Exception {
        roomRepository.deleteAll();
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
                .andReturn();
        id = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), RoomResponseDto.class).getId();
    }

    @Test
    @WithMockUser(roles = "USER_ROLE")
    void updateRoom() throws Exception {
        RoomUpdateRequestDto updateRequestDto = RoomUpdateRequestDto.builder()
                .title("update_title")
                .address("update_addr")
                .phone("update_phone")
                .link("update_link")
                .description("update_desc")
                .build();
        MvcResult mvcResult = mockMvc.perform(put("/rooms/"+id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequestDto)))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
        RoomResponseDto responseDto = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), RoomResponseDto.class);
        assertThat(responseDto.getTitle()).isEqualTo(updateRequestDto.getTitle());
        assertThat(responseDto.getAddress()).isEqualTo(updateRequestDto.getAddress());
        assertThat(responseDto.getPhone()).isEqualTo(updateRequestDto.getPhone());
        assertThat(responseDto.getLink()).isEqualTo(updateRequestDto.getLink());
        assertThat(responseDto.getDescription()).isEqualTo(updateRequestDto.getDescription());
    }

}
