package com.bangstagram.room.service;

import com.bangstagram.room.controller.dto.request.ThemeSaveRequestDto;
import com.bangstagram.room.controller.dto.request.ThemeUpdateRequestDto;
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

import java.util.List;

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
        // given
        Room room = Room.builder()
                .title("title")
                .address("addr")
                .link("link")
                .phone("phone")
                .description("desc")
                .build();
        Room savedRoom = roomRepository.save(room);

        Theme theme = Theme.builder()
                .title("newTheme")
                .genre("genre")
                .description("desc")
                .imgSrc("src")
                .build();
        theme.setRoom(savedRoom);
        Theme savedTheme = themeRepository.save(theme);

        // when
        ThemeResponseDto responseDto = themeService.findById(savedTheme.getId());

        // then
        assertThat(responseDto.getTitle()).isEqualTo(savedTheme.getTitle());
        assertThat(responseDto.getRoomId()).isEqualTo(savedTheme.getRoom().getId());
        assertThat(responseDto.getDescription()).isEqualTo(savedTheme.getDescription());
        assertThat(responseDto.getGenre()).isEqualTo(savedTheme.getGenre());
        assertThat(responseDto.getImgSrc()).isEqualTo(savedTheme.getImgSrc());

    }

    @Test
    @DisplayName("방탈출 id로 테마 조회")
    void findByRoomId() {
        // given
        Room room = Room.builder()
                .title("title")
                .address("addr")
                .link("link")
                .phone("phone")
                .description("desc")
                .build();
        Room savedRoom = roomRepository.save(room);

        Theme theme = Theme.builder()
                .title("newTheme")
                .genre("genre")
                .description("desc")
                .imgSrc("src")
                .build();
        theme.setRoom(savedRoom);
        Theme savedTheme = themeRepository.save(theme);

        // when
        List<ThemeResponseDto> themeResponseDtos = themeService.findByRoomId(savedRoom.getId());

        // then
        assertThat(themeResponseDtos.get(0).getRoomId()).isEqualTo(savedTheme.getRoom().getId());
        assertThat(themeResponseDtos.get(0).getTitle()).isEqualTo(savedTheme.getTitle());
        assertThat(themeResponseDtos.get(0).getImgSrc()).isEqualTo(savedTheme.getImgSrc());
        assertThat(themeResponseDtos.get(0).getGenre()).isEqualTo(savedTheme.getGenre());
        assertThat(themeResponseDtos.get(0).getDescription()).isEqualTo(savedTheme.getDescription());
    }

    @Test
    @DisplayName("테마 정보 생성 테스트")
    void createTheme() {
        // given
        Room room = Room.builder()
                .title("title")
                .address("addr")
                .link("link")
                .phone("phone")
                .description("desc")
                .build();
        Room savedRoom = roomRepository.save(room);
        ThemeSaveRequestDto requestDto = ThemeSaveRequestDto.builder()
                .title("title")
                .roomId(savedRoom.getId())
                .imgSrc("imgSrc")
                .description("desc")
                .genre("genre")
                .build();

        // when
        ThemeResponseDto responseDto = themeService.createTheme(savedRoom.getId(), requestDto);

        // then
        assertThat(responseDto.getTitle()).isEqualTo(requestDto.getTitle());
        assertThat(responseDto.getRoomId()).isEqualTo(requestDto.getRoomId());
        assertThat(responseDto.getDescription()).isEqualTo(requestDto.getDescription());
        assertThat(responseDto.getGenre()).isEqualTo(requestDto.getGenre());
        assertThat(responseDto.getImgSrc()).isEqualTo(requestDto.getImgSrc());

    }

    @Test
    void updateTheme() {
        // given
        Room room = Room.builder()
                .title("title")
                .address("addr")
                .link("link")
                .phone("phone")
                .description("desc")
                .build();
        Room savedRoom = roomRepository.save(room);

        Theme theme = Theme.builder()
                .title("theme")
                .genre("genre")
                .description("desc")
                .imgSrc("src")
                .build();
        theme.setRoom(savedRoom);
        Theme savedTheme = themeRepository.save(theme);

        ThemeUpdateRequestDto requestDto = ThemeUpdateRequestDto.builder()
                .title("newTheme")
                .genre("newGenre")
                .imgSrc("newSrc")
                .description("newDesc")
                .build();

        // when
        ThemeResponseDto responseDto = themeService.updateTheme(savedRoom.getId(), savedTheme.getId(), requestDto);

        // then
        assertThat(responseDto.getTitle()).isEqualTo(requestDto.getTitle());
        assertThat(responseDto.getImgSrc()).isEqualTo(requestDto.getImgSrc());
        assertThat(responseDto.getGenre()).isEqualTo(requestDto.getGenre());
        assertThat(responseDto.getDescription()).isEqualTo(requestDto.getDescription());
    }
}