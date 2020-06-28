package com.bangstagram.room.controller.dto.request;

import com.bangstagram.room.domain.model.Review;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
public class ReviewSaveRequestDto {
    @NotNull
    private Long userId;

    @NotNull
    private Long themeId;

    private double score;

    @NotBlank
    private String level;

    private boolean success;

    private String content;
    private int leftTime;
    private int hintCount;

    public ReviewSaveRequestDto() {
    }

    @Builder
    public ReviewSaveRequestDto(Long userId, Long themeId, String content, double score, String level, boolean success, int leftTime, int hintCount) {
        this.userId = userId;
        this.themeId = themeId;
        this.content = content;
        this.score = score;
        this.level = level;
        this.success = success;
        this.leftTime = leftTime;
        this.hintCount = hintCount;
    }

    public Review toEntity() {
        return Review.builder()
                .userId(userId)
                .content(content)
                .score(score)
                .level(level)
                .success(success)
                .leftTime(leftTime)
                .hintCount(hintCount)
                .build();
    }
}
