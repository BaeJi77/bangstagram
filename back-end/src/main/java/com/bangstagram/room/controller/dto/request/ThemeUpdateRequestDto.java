package com.bangstagram.room.controller.dto.request;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class ThemeUpdateRequestDto {
    @NotBlank
    private String title;

    private String imgSrc;
    private String description;
    private String genre;

    public ThemeUpdateRequestDto() {
    }

    @Builder
    public ThemeUpdateRequestDto(@NotBlank String title, String imgSrc, String description, String genre) {
        this.title = title;
        this.imgSrc = imgSrc;
        this.description = description;
        this.genre = genre;
    }
}
