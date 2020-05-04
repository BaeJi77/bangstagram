package com.bangstagram.room.service;

import com.bangstagram.room.controller.dto.RoomResponseDto;
import com.bangstagram.room.controller.dto.RoomSaveRequestDto;
import com.bangstagram.room.controller.dto.RoomUpdateRequestDto;
import com.bangstagram.room.domain.model.Room;
import com.bangstagram.room.domain.repository.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class RoomServiceTest {
    @Autowired
    private RoomService roomService;

    @Autowired
    private RoomRepository roomRepository;

    @BeforeEach
    void setUp() {
        roomRepository.deleteAll();
    }

    @Test
    @DisplayName("방탈출 리스트 조회 테스트: 저장하고 조회해서 비교")
    void findAll() {
        Room savedRoom = roomRepository.save(Room.builder()
                .title("title")
                .address("addr")
                .link("link")
                .phone("phone")
                .description("desc")
                .build());
        List<RoomResponseDto> responseDtoList = roomService.findAll();
        assertThat(responseDtoList.get(0).getTitle()).isEqualTo(savedRoom.getTitle());
        assertThat(responseDtoList.get(0).getAddress()).isEqualTo(savedRoom.getAddress());
        assertThat(responseDtoList.get(0).getLink()).isEqualTo(savedRoom.getLink());
        assertThat(responseDtoList.get(0).getPhone()).isEqualTo(savedRoom.getPhone());
        assertThat(responseDtoList.get(0).getDescription()).isEqualTo(savedRoom.getDescription());
    }

    @Test
    @DisplayName("방탈출 id로 조회 테스트")
    void findById() {
        Room savedRoom = roomRepository.save(Room.builder()
                .title("title")
                .address("addr")
                .link("link")
                .phone("phone")
                .description("desc")
                .build());
        RoomResponseDto responseDto = roomService.findById(savedRoom.getId());
        assertThat(responseDto.getTitle()).isEqualTo(savedRoom.getTitle());
        assertThat(responseDto.getAddress()).isEqualTo(savedRoom.getAddress());
        assertThat(responseDto.getLink()).isEqualTo(savedRoom.getLink());
        assertThat(responseDto.getPhone()).isEqualTo(savedRoom.getPhone());
        assertThat(responseDto.getDescription()).isEqualTo(savedRoom.getDescription());
    }

    @Test
    @DisplayName("방탈출 정보 생성 테스트")
    void createRoom() {
        RoomSaveRequestDto saveRequestDto = RoomSaveRequestDto.builder()
                .title("title")
                .address("addr")
                .link("link")
                .phone("phone")
                .description("desc")
                .build();
        RoomResponseDto responseDto = roomService.createRoom(saveRequestDto);
        assertThat(responseDto.getTitle()).isEqualTo(saveRequestDto.getTitle());
        assertThat(responseDto.getAddress()).isEqualTo(saveRequestDto.getAddress());
        assertThat(responseDto.getLink()).isEqualTo(saveRequestDto.getLink());
        assertThat(responseDto.getPhone()).isEqualTo(saveRequestDto.getPhone());
        assertThat(responseDto.getDescription()).isEqualTo(saveRequestDto.getDescription());
    }

    @Test
    @DisplayName("방탈출 정보 수정 테스트")
    void updateRoom() {
        Room savedRoom = roomRepository.save(Room.builder()
                .title("title")
                .address("addr")
                .link("link")
                .phone("phone")
                .description("desc")
                .build());
        RoomUpdateRequestDto updateRequestDto = RoomUpdateRequestDto.builder()
                .title("title2")
                .address("addr2")
                .link("link2")
                .phone("phone2")
                .description("desc2")
                .build();
        RoomResponseDto responseDto = roomService.updateRoom(savedRoom.getId(), updateRequestDto);
        assertThat(responseDto.getTitle()).isEqualTo(updateRequestDto.getTitle());
        assertThat(responseDto.getAddress()).isEqualTo(updateRequestDto.getAddress());
        assertThat(responseDto.getLink()).isEqualTo(updateRequestDto.getLink());
        assertThat(responseDto.getPhone()).isEqualTo(updateRequestDto.getPhone());
        assertThat(responseDto.getDescription()).isEqualTo(updateRequestDto.getDescription());
    }
}
