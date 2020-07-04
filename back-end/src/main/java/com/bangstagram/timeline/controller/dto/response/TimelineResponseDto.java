package com.bangstagram.timeline.controller.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * author: Ji-Hoon Bae
 * Date: 2020.04.28
 */

@Getter
public class TimelineResponseDto {
    private Long id;
    private String title;
    private String body;
    private LocalDateTime createdAt;
    private Long userId;
    private Long roomId;
    private List<TimelineCommentResponseDto> timelineComments = new ArrayList<>();

    public TimelineResponseDto() {
    }

    @Builder
    public TimelineResponseDto(Long id, String title, String body, LocalDateTime createdAt, Long userId, Long roomId, List<TimelineCommentResponseDto> timelineComments) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.createdAt = createdAt;
        this.userId = userId;
        this.roomId = roomId;
    }

    @Override
    public String toString() {
        return "TimelineResponseDto{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", body='" + body + '\'' +
                ", createdAt=" + createdAt +
                ", userId=" + userId +
                ", roomId=" + roomId +
                ", timelineComments=" + timelineComments +
                '}';
    }
}
