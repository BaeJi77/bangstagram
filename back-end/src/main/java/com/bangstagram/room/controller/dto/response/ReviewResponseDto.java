package com.bangstagram.room.controller.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ReviewResponseDto {
    private Long id;
    private Long userId;
    private Long themeId;
    private String content;
    private double score;
    private String level;
    private boolean success;
    private int leftTime;
    private int hintCount;

    public ReviewResponseDto() {
    }

    @Builder
    public ReviewResponseDto(Long id, Long userId, Long themeId, String content, double score, String level, boolean success, int leftTime, int hintCount) {
        this.id = id;
        this.userId = userId;
        this.themeId = themeId;
        this.content = content;
        this.score = score;
        this.level = level;
        this.success = success;
        this.leftTime = leftTime;
        this.hintCount = hintCount;
    }
}
