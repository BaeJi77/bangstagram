package com.bangstagram.room.controller.dto.response;

import com.bangstagram.room.domain.model.Theme;
import lombok.Builder;
import lombok.Getter;

import java.util.List;


@Getter
public class RoomResponseDto {
    private Long id;
    private String title;
    private String address;
    private String link;
    private String phone;
    private String description;
    private List<Theme> themes;

    public RoomResponseDto() {
    }

    @Builder
    public RoomResponseDto(Long id, String title, String link, String phone, String address, String description, List<Theme> themes) {
        this.id = id;
        this.title = title;
        this.link = link;
        this.phone = phone;
        this.address = address;
        this.description = description;
        this.themes = themes;
    }
}
