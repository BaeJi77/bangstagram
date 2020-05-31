package com.bangstagram.room.controller.dto.request;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
public class RoomUpdateRequestDto {
    @NotBlank
    private String title;

    @NotBlank
    private String address;
    private String link;
    private String phone;
    private String description;
    private List<ThemeUpdateRequestDto> themes;

    public RoomUpdateRequestDto() {
    }

    @Builder
    public RoomUpdateRequestDto(String title, String link, String phone, String address, String description, List<ThemeUpdateRequestDto> themes) {
        this.title = title;
        this.link = link;
        this.phone = phone;
        this.address = address;
        this.description = description;
        this.themes = themes;
    }
}
