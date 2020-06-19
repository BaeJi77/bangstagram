package com.bangstagram.room.controller;

import com.bangstagram.room.controller.dto.request.ThemeSaveRequestDto;
import com.bangstagram.room.controller.dto.request.ThemeUpdateRequestDto;
import com.bangstagram.room.controller.dto.response.ThemeResponseDto;
import com.bangstagram.room.service.ThemeService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class ThemeController {
    private ThemeService themeService;

    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @GetMapping("/rooms/{roomId}/themes/{themeId}")
    public ThemeResponseDto findThemeById(@PathVariable Long roomId, @PathVariable Long themeId) {
        return themeService.findById(themeId);
    }

    @GetMapping("/rooms/{roomId}/themes")
    public List<ThemeResponseDto> findThemesByRoomId(@PathVariable Long roomId) {
        return themeService.findByRoomId(roomId);
    }

    @PostMapping("/rooms/{roomId}/themes")
    public ThemeResponseDto createTheme(@PathVariable Long roomId, @RequestBody @Valid ThemeSaveRequestDto requestDto) {
        return themeService.createTheme(roomId, requestDto);
    }

    @PutMapping("/rooms/{roomId}/themes/{themeId}")
    public ThemeResponseDto updateTheme(@PathVariable Long roomId, @PathVariable Long themeId, @RequestBody @Valid ThemeUpdateRequestDto requestDto) {
        return themeService.updateTheme(roomId, themeId, requestDto);
    }
}
