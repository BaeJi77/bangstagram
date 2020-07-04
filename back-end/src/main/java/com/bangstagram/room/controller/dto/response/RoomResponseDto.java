package com.bangstagram.room.controller.dto.response;

import lombok.Builder;
import lombok.Getter;


@Getter
public class RoomResponseDto {
    private Long id;
    private String title;
    private String address;
    private String link;
    private String phone;
    private String description;

    public RoomResponseDto() {
    }

    @Builder
    public RoomResponseDto(Long id, String title, String link, String phone, String address, String description) {
        this.id = id;
        this.title = title;
        this.link = link;
        this.phone = phone;
        this.address = address;
        this.description = description;
    }
}
