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

import static org.assertj.core.api.BDDAssertions.then;

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
        then(responseDtoList.get(0).getTitle()).isEqualTo(savedRoom.getTitle());
        then(responseDtoList.get(0).getAddress()).isEqualTo(savedRoom.getAddress());
        then(responseDtoList.get(0).getLink()).isEqualTo(savedRoom.getLink());
        then(responseDtoList.get(0).getPhone()).isEqualTo(savedRoom.getPhone());
        then(responseDtoList.get(0).getDescription()).isEqualTo(savedRoom.getDescription());
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
        then(responseDto.getTitle()).isEqualTo(savedRoom.getTitle());
        then(responseDto.getAddress()).isEqualTo(savedRoom.getAddress());
        then(responseDto.getLink()).isEqualTo(savedRoom.getLink());
        then(responseDto.getPhone()).isEqualTo(savedRoom.getPhone());
        then(responseDto.getDescription()).isEqualTo(savedRoom.getDescription());
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
        then(responseDto.getTitle()).isEqualTo(saveRequestDto.getTitle());
        then(responseDto.getAddress()).isEqualTo(saveRequestDto.getAddress());
        then(responseDto.getLink()).isEqualTo(saveRequestDto.getLink());
        then(responseDto.getPhone()).isEqualTo(saveRequestDto.getPhone());
        then(responseDto.getDescription()).isEqualTo(saveRequestDto.getDescription());
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
        then(responseDto.getTitle()).isEqualTo(updateRequestDto.getTitle());
        then(responseDto.getAddress()).isEqualTo(updateRequestDto.getAddress());
        then(responseDto.getLink()).isEqualTo(updateRequestDto.getLink());
        then(responseDto.getPhone()).isEqualTo(updateRequestDto.getPhone());
        then(responseDto.getDescription()).isEqualTo(updateRequestDto.getDescription());
    }

    @Test
    @DisplayName("방탈출 지역별 검색 테스트: 데이터베이스 검색")
    void findRoomByRegion() {
        Room savedRoom = roomRepository.save(Room.builder().title("room_title").address("서울특별시 강남구 논현1동").build());
        List<RoomResponseDto> responseDtos = roomService.findRoomByRegion("강남");
        then(responseDtos.get(0).getTitle()).isEqualTo(savedRoom.getTitle());
    }
}
