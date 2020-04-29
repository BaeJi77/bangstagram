package com.bangstagram.room.controller.dto;

import com.bangstagram.room.domain.model.Room;
import lombok.Builder;
import lombok.Getter;

@Getter
public class RoomSaveRequestDto {
    private String title;
    private String link;
    private String phone;
    private String address;
    private String description;

    public RoomSaveRequestDto(){}

    @Builder
    public RoomSaveRequestDto(String title, String link, String phone, String address, String description) {
        this.title = title;
        this.link = link;
        this.phone = phone;
        this.address = address;
        this.description = description;
    }

    public Room toEntity() {
        return Room.builder()
                .title(title)
                .link(link)
                .phone(phone)
                .address(address)
                .description(description)
                .build();
    }
}
