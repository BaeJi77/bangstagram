package com.bangstagram.room.service;

import com.bangstagram.common.exception.DoNotExistException;
import com.bangstagram.room.controller.dto.request.ThemeSaveRequestDto;
import com.bangstagram.room.controller.dto.request.ThemeUpdateRequestDto;
import com.bangstagram.room.controller.dto.response.ThemeResponseDto;
import com.bangstagram.room.domain.model.Room;
import com.bangstagram.room.domain.model.Theme;
import com.bangstagram.room.domain.repository.RoomRepository;
import com.bangstagram.room.domain.repository.ThemeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class ThemeService {
    private ThemeRepository themeRepository;
    private RoomRepository roomRepository;

    public ThemeService(ThemeRepository themeRepository, RoomRepository roomRepository) {
        this.themeRepository = themeRepository;
        this.roomRepository = roomRepository;
    }

    @Transactional(readOnly = true)
    public ThemeResponseDto findById(Long id) {
        Theme theme = themeRepository.findById(id).orElseThrow(() -> new DoNotExistException("테마 정보가 없습니다."));

        return ThemeResponseDto.builder()
                .id(theme.getId())
                .title(theme.getTitle())
                .imgSrc(theme.getImgSrc())
                .description(theme.getDescription())
                .genre(theme.getGenre())
                .build();
    }

    @Transactional
    public ThemeResponseDto createTheme(ThemeSaveRequestDto requestDto) {
        log.info("[create Theme] room_id={}", requestDto.getRoomId());
        Theme theme = themeRepository.save(requestDto.toEntity());
        Room room = roomRepository.findById(requestDto.getRoomId()).orElseThrow(() ->
                new DoNotExistException("방탈출 정보가 없습니다."));
        room.addTheme(theme);

        return ThemeResponseDto.builder()
                .id(theme.getId())
                .title(theme.getTitle())
                .imgSrc(theme.getImgSrc())
                .description(theme.getDescription())
                .genre(theme.getGenre())
                .build();
    }
}
