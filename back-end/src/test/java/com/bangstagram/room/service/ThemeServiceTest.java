package com.bangstagram.room.service;

import com.bangstagram.room.controller.dto.request.ThemeSaveRequestDto;
import com.bangstagram.room.controller.dto.response.ThemeResponseDto;
import com.bangstagram.room.domain.model.Room;
import com.bangstagram.room.domain.model.Theme;
import com.bangstagram.room.domain.repository.RoomRepository;
import com.bangstagram.room.domain.repository.ThemeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ThemeServiceTest {
    @Autowired
    private ThemeService themeService;

    @Autowired
    private ThemeRepository themeRepository;

    @Autowired
    private RoomRepository roomRepository;

    @BeforeEach
    void setup() {
        themeRepository.deleteAll();
        roomRepository.deleteAll();
    }

    @Test
    @DisplayName("방탈출 테마 id로 조회 테스트")
    void findById() {
        //given
        Room room = Room.builder()
                .title("title")
                .address("addr")
                .link("link")
                .phone("phone")
                .description("desc")
                .build();
        room.addThemes(Collections.emptyList());
        Theme savedTheme = themeRepository.save(Theme.builder()
                .title("title")
                .imgSrc("imgSrc")
                .description("desc")
                .genre("genre")
                .build());

        //when
        ThemeResponseDto responseDto = themeService.findById(savedTheme.getId());

        //then
        assertThat(responseDto.getTitle()).isEqualTo(savedTheme.getTitle());
        assertThat(responseDto.getDescription()).isEqualTo(savedTheme.getDescription());
        assertThat(responseDto.getRoomId()).isEqualTo(room.getId());
    }

    @Test
    @DisplayName("테마 정보 생성 테스트")
    void createTheme() {
        //given
        Room room = Room.builder()
                .title("title")
                .address("addr")
                .link("link")
                .phone("phone")
                .description("desc")
                .build();
        room.addThemes(Collections.emptyList());
        Room savedRoom = roomRepository.save(room);
        ThemeSaveRequestDto requestDto = ThemeSaveRequestDto.builder()
                .title("title")
                .roomId(savedRoom.getId())
                .imgSrc("imgSrc")
                .description("desc")
                .genre("genre")
                .build();

        //when
        ThemeResponseDto responseDto = themeService.createTheme(requestDto);

        //then
        assertThat(requestDto.getTitle()).isEqualTo(responseDto.getTitle());
        assertThat(requestDto.getRoomId()).isEqualTo(responseDto.getRoomId());
    }
}