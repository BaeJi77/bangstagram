package com.bangstagram.timeline.dto;

import com.bangstagram.timeline.domain.model.Timeline;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ToString
@Getter
public class TimelineRequestDto {
    @NotBlank
    private String title;

    @NotBlank
    private String body;

    @NotNull
    private Long userId;

    @NotNull
    private Long roomId;

    public TimelineRequestDto() {
    }

    public TimelineRequestDto(String title, String body, Long userId, Long roomId) {
        this.title = title;
        this.body = body;
        this.userId = userId;
        this.roomId = roomId;
    }

    public Timeline convertToTimeline() {
        return Timeline.builder()
                .title(this.title)
                .body(this.body)
                .userId(this.userId)
                .roomId(this.roomId)
                .build();
    }
}
