package com.bangstagram.room.controller.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class RoomUpdateRequestDto {
    private String title;
    private String link;
    private String phone;
    private String address;
    private String description;

    public RoomUpdateRequestDto(){}

    @Builder
    public RoomUpdateRequestDto(String title, String link, String phone, String address, String description) {
        this.title = title;
        this.link = link;
        this.phone = phone;
        this.address = address;
        this.description = description;
    }
}
