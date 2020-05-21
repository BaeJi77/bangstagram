package com.bangstagram.room.controller;

import com.bangstagram.room.controller.dto.request.ThemeSaveRequestDto;
import com.bangstagram.room.controller.dto.request.ThemeUpdateRequestDto;
import com.bangstagram.room.controller.dto.response.ThemeResponseDto;
import com.bangstagram.room.service.ThemeService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class ThemeController {
    private ThemeService themeService;

    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @GetMapping("/themes/{id}")
    public ThemeResponseDto findThemeById(@PathVariable Long id) {
        return themeService.findById(id);
    }

    @PostMapping("/themes")
    public ThemeResponseDto createTheme(@RequestBody @Valid ThemeSaveRequestDto requestDto) {
        return themeService.createTheme(requestDto);
    }
}
