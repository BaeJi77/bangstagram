package com.bangstagram.room.service;

import com.bangstagram.room.controller.dto.request.RoomSaveRequestDto;
import com.bangstagram.room.controller.dto.request.RoomUpdateRequestDto;
import com.bangstagram.room.controller.dto.request.ThemeSaveRequestDto;
import com.bangstagram.room.controller.dto.response.RoomResponseDto;
import com.bangstagram.room.domain.model.Room;
import com.bangstagram.room.domain.repository.RoomRepository;
import com.bangstagram.room.domain.repository.ThemeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class RoomServiceTest {
    @Autowired
    private RoomService roomService;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private ThemeRepository themeRepository;

    @BeforeEach
    void setUp() {
        roomRepository.deleteAll();
        themeRepository.deleteAll();
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

    @Test
    @DisplayName("방탈출 지역별 검색 테스트: 데이터베이스 검색")
    void findRoomByRegion() {
        Room savedRoom = roomRepository.save(Room.builder().title("room_title").address("서울 서초구 서초대로77길").build());
        List<RoomResponseDto> responseDtos = roomService.findRoomByRegion("서초");
        assertThat(responseDtos.get(0).getTitle()).isEqualTo(savedRoom.getTitle());
    }

    @Test
    void deleteRoom() {
        //given
        Room room = Room.builder()
                .title("room_title")
                .address("address")
                .phone("phone")
                .description("desc")
                .link("link")
                .build();
        Room savedRoom = roomRepository.save(room);

        List<ThemeSaveRequestDto> requestDtos = new ArrayList<>();
        IntStream.rangeClosed(1, 5).forEach(i -> requestDtos.add(ThemeSaveRequestDto.builder()
                .title("title" + i)
                .imgSrc("imgSrc" + i)
                .description("desc" + i)
                .genre("genre" + i)
                .build()));
        roomService.addThemes(savedRoom.getId(), requestDtos);

        //when
        roomService.deleteRoom(savedRoom.getId());

        //then
        assertThat(themeRepository.findAll()).isEmpty();
    }

}
