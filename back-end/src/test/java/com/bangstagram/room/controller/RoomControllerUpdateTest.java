package com.bangstagram.room.controller;

import com.bangstagram.room.controller.dto.RoomResponseDto;
import com.bangstagram.room.controller.dto.RoomSaveRequestDto;
import com.bangstagram.room.controller.dto.RoomUpdateRequestDto;
import com.bangstagram.room.domain.repository.RoomRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.BDDAssertions.then;
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
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn();
        RoomResponseDto responseDto = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), RoomResponseDto.class);
        then(responseDto.getTitle()).isEqualTo(updateRequestDto.getTitle());
        then(responseDto.getAddress()).isEqualTo(updateRequestDto.getAddress());
        then(responseDto.getPhone()).isEqualTo(updateRequestDto.getPhone());
        then(responseDto.getLink()).isEqualTo(updateRequestDto.getLink());
        then(responseDto.getDescription()).isEqualTo(updateRequestDto.getDescription());
    }

}
