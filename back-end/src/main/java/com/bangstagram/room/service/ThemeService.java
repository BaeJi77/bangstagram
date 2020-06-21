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

import java.util.List;
import java.util.stream.Collectors;

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
        log.info("[find Theme By id] id={}", id);
        Theme theme = themeRepository.findById(id).orElseThrow(() -> new DoNotExistException("테마 정보가 없습니다."));

        return ThemeResponseDto.builder()
                .id(theme.getId())
                .roomId(theme.getRoom().getId())
                .title(theme.getTitle())
                .imgSrc(theme.getImgSrc())
                .description(theme.getDescription())
                .genre(theme.getGenre())
                .build();
    }

    @Transactional(readOnly = true)
    public List<ThemeResponseDto> findByRoomId(Long roomId) {
        log.info("[find Theme By RoomId] roomId={}", roomId);
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new DoNotExistException("방탈출 정보가 없습니다."));
        List<Theme> themes = themeRepository.findByRoomId(roomId);

        return themes.stream().map(theme -> ThemeResponseDto.builder()
                .id(theme.getId())
                .roomId(theme.getRoom().getId())
                .title(theme.getTitle())
                .genre(theme.getGenre())
                .imgSrc(theme.getImgSrc())
                .description(theme.getDescription())
                .build()).collect(Collectors.toList());
    }

    @Transactional
    public ThemeResponseDto createTheme(Long roomId, ThemeSaveRequestDto requestDto) {
        log.info("[create Theme] room_id={}", requestDto.getRoomId());
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new DoNotExistException("방탈출 정보가 없습니다."));
        Theme newTheme = requestDto.toEntity();
        newTheme.setRoom(room);
        Theme savedTheme = themeRepository.save(newTheme);

        return ThemeResponseDto.builder()
                .id(savedTheme.getId())
                .roomId(roomId)
                .title(savedTheme.getTitle())
                .imgSrc(savedTheme.getImgSrc())
                .description(savedTheme.getDescription())
                .genre(savedTheme.getGenre())
                .build();
    }

    @Transactional
    public ThemeResponseDto updateTheme(Long roomId, Long themeId, ThemeUpdateRequestDto requestDto) {
        log.info("[update Theme] id={}, {}", themeId, requestDto);
        Theme theme = themeRepository.findById(themeId).orElseThrow(() -> new DoNotExistException("테마 정보가 없습니다."));
        theme.update(requestDto.getTitle(), requestDto.getImgSrc(), requestDto.getDescription(), requestDto.getGenre());

        return ThemeResponseDto.builder()
                .id(theme.getId())
                .roomId(theme.getRoom().getId())
                .title(theme.getTitle())
                .imgSrc(theme.getImgSrc())
                .description(theme.getDescription())
                .genre(theme.getGenre())
                .build();
    }
}
