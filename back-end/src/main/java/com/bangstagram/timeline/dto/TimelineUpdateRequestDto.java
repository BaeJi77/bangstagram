package com.bangstagram.timeline.dto;

import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Getter
@ToString
public class TimelineUpdateRequestDto {
    @NotBlank
    private String title;

    @NotBlank
    private String body;

    public TimelineUpdateRequestDto() {
    }

    public TimelineUpdateRequestDto(String title, String body) {
        this.title = title;
        this.body = body;
    }
}
