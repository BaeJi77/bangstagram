package com.bangstagram.room.controller.dto.request;

import com.bangstagram.room.domain.model.Room;
import com.bangstagram.room.domain.model.Theme;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
public class RoomSaveRequestDto {
    @NotBlank
    private String title;

    @NotBlank
    private String address;
    private String link;
    private String phone;
    private String description;
    private List<Theme> themes;

    public RoomSaveRequestDto() {
    }

    @Builder
    public RoomSaveRequestDto(String title, String link, String phone, String address, String description, List<Theme> themes) {
        this.title = title;
        this.link = link;
        this.phone = phone;
        this.address = address;
        this.description = description;
        this.themes = themes;
    }

    public Room toEntity() {
        return Room.builder()
                .title(title)
                .link(link)
                .phone(phone)
                .address(address)
                .description(description)
                .themes(themes)
                .build();
    }
}
