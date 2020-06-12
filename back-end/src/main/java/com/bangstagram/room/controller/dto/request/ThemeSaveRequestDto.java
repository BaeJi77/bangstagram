package com.bangstagram.room.controller.dto.request;

import com.bangstagram.room.domain.model.Theme;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
public class ThemeSaveRequestDto {
    @NotBlank
    private String title;

    @NotNull
    private Long roomId;

    private String imgSrc;
    private String description;
    private String genre;

    public ThemeSaveRequestDto() {
    }

    @Builder
    public ThemeSaveRequestDto(@NotBlank String title, @NotNull Long roomId, String imgSrc, String description, String genre) {
        this.title = title;
        this.roomId = roomId;
        this.imgSrc = imgSrc;
        this.description = description;
        this.genre = genre;
    }

    public Theme toEntity() {
        return Theme.builder()
                .title(title)
                .imgSrc(imgSrc)
                .description(description)
                .genre(genre)
                .build();
    }
}
