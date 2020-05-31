package com.bangstagram.room.controller.dto.request;

import com.bangstagram.room.domain.model.Room;
import com.bangstagram.room.domain.model.Theme;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class RoomSaveRequestDto {
    @NotBlank
    private String title;

    @NotBlank
    private String address;
    private String link;
    private String phone;
    private String description;
    private List<ThemeSaveRequestDto> themes;

    public RoomSaveRequestDto() {
    }

    @Builder
    public RoomSaveRequestDto(String title, String link, String phone, String address, String description, List<ThemeSaveRequestDto> themes) {
        this.title = title;
        this.link = link;
        this.phone = phone;
        this.address = address;
        this.description = description;
        this.themes = themes;
    }

    public Room toEntity() {
        Room room = Room.builder()
                .title(title)
                .link(link)
                .phone(phone)
                .address(address)
                .description(description)
                .build();
        if (themes == null) {
            themes = new ArrayList<>();
        }
        room.addThemes(themes.stream().map(themeDto -> Theme.builder()
                .title(themeDto.getTitle())
                .imgSrc(themeDto.getImgSrc())
                .description(themeDto.getDescription())
                .genre(themeDto.getDescription())
                .build())
                .collect(Collectors.toList()));
        return room;
    }
}
