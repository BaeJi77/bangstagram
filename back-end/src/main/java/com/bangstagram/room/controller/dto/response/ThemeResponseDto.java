package com.bangstagram.room.controller.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ThemeResponseDto {
    private Long id;
    private Long roomId;
    private String title;
    private String imgSrc;
    private String description;
    private String genre;

    public ThemeResponseDto() {
    }

    @Builder
    public ThemeResponseDto(Long id, Long roomId, String title, String imgSrc, String description, String genre) {
        this.id = id;
        this.roomId = roomId;
        this.title = title;
        this.imgSrc = imgSrc;
        this.description = description;
        this.genre = genre;
    }
}
