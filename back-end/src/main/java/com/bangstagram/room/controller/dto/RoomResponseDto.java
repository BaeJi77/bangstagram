package com.bangstagram.room.controller.dto;

import com.bangstagram.room.domain.model.Room;
import lombok.Builder;
import lombok.Getter;

@Getter
public class RoomResponseDto {
    private String title;
    private String link;
    private String phone;
    private String description;
    private String address;

    public RoomResponseDto() {}

    @Builder
    public RoomResponseDto(String title, String link, String phone, String description, String address) {
        this.title = title;
        this.link = link;
        this.phone = phone;
        this.description = description;
        this.address = address;
    }

    public RoomResponseDto(Room room) {
        this.title = room.getTitle();
        this.link = room.getLink();
        this.address = room.getAddress();
        this.phone = room.getPhone();
        this.description = room.getDescription();
    }

    public Room toEntity() {
        return Room.builder()
                .title(title)
                .link(link)
                .address(address)
                .phone(phone)
                .description(description)
                .build();
    }
}
