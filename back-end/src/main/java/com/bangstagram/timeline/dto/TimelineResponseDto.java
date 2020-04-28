package com.bangstagram.timeline.dto;

import com.bangstagram.timeline.domain.model.Timeline;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;


@Getter
@ToString
public class TimelineResponseDto {
    private Long id;
    private String title;
    private String body;
    private LocalDateTime createdAt;
    private Long userId;
    private Long roomId;

    public TimelineResponseDto() {
    }

    public TimelineResponseDto(Long id, String title, String body, LocalDateTime createdAt, Long userId, Long roomId) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.createdAt = createdAt;
        this.userId = userId;
        this.roomId = roomId;
    }

    public TimelineResponseDto(Timeline timeline) {
        this.id = timeline.getId();
        this.body = timeline.getBody();
        this.title = timeline.getTitle();
        this.createdAt = timeline.getCreatedAt();
        this.userId = timeline.getRoomId();
        this.roomId = timeline.getRoomId();
    }
}
