package com.bangstagram.room.controller.dto.request;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
public class ThemeUpdateRequestDto {
    @NotBlank
    private String title;

    @NotNull
    private Long roomId;

    private String imgSrc;
    private String description;
    private String genre;

    public ThemeUpdateRequestDto() {
    }

    @Builder
    public ThemeUpdateRequestDto(@NotBlank String title, @NotNull Long roomId, String imgSrc, String description, String genre) {
        this.title = title;
        this.roomId = roomId;
        this.imgSrc = imgSrc;
        this.description = description;
        this.genre = genre;
    }
}
