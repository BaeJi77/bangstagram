package com.bangstagram.timeline.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

/**
 author: Ji-Hoon Bae
 Date: 2020.04.29
 */

@Getter
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

    @Override
    public String toString() {
        return "TimelineUpdateRequestDto{" +
                "title='" + title + '\'' +
                ", body='" + body + '\'' +
                '}';
    }
}
