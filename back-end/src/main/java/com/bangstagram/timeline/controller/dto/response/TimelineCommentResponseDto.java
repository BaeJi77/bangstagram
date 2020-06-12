package com.bangstagram.timeline.controller.dto.response;

import lombok.Builder;
import lombok.Getter;

/**
 * author: Ji-Hoon Bae
 * Date: 2020.05.29
 */

@Getter
public class TimelineCommentResponseDto {
    private Long id;
    private String comment;
    private Long userId;
    private Long timelineId;

    public TimelineCommentResponseDto() {
    }

    @Builder
    public TimelineCommentResponseDto(Long id, String comment, Long userId, Long timelineId) {
        this.id = id;
        this.comment = comment;
        this.userId = userId;
        this.timelineId = timelineId;
    }

    @Override
    public String toString() {
        return "TimelineCommentResponseDto{" +
                "id=" + id +
                ", comment='" + comment + '\'' +
                ", userId=" + userId +
                ", timelineId=" + timelineId +
                '}';
    }
}
