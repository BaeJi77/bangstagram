package com.bangstagram.room.controller.dto.request;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class ReviewUpdateRequestDto {
    @NotNull
    private double score;

    @NotNull
    private String level;

    @NotNull
    private boolean success;

    private String content;
    private int leftTime;
    private int hintCount;

    public ReviewUpdateRequestDto() {
    }

    @Builder
    public ReviewUpdateRequestDto(double score, String level, boolean success, String content, int leftTime, int hintCount) {
        this.score = score;
        this.level = level;
        this.success = success;
        this.content = content;
        this.leftTime = leftTime;
        this.hintCount = hintCount;
    }
}
