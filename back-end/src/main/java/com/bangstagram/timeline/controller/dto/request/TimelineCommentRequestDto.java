package com.bangstagram.timeline.controller.dto.request;

import com.bangstagram.timeline.domain.model.TimelineComment;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
public class TimelineCommentRequestDto {
    @NotBlank
    private String comment;

    @NotNull
    private Long userId;

    @NotNull
    private Long timelineId;

    public TimelineCommentRequestDto() {
    }

    public TimelineCommentRequestDto(String comment, Long userId, Long timelineId) {
        this.comment = comment;
        this.userId = userId;
        this.timelineId = timelineId;
    }

    public static TimelineComment convertToTimelineComment(TimelineCommentRequestDto timelineCommentRequestDto) {
        return TimelineComment.builder()
                .comment(timelineCommentRequestDto.comment)
                .userId(timelineCommentRequestDto.userId)
                .timelineId(timelineCommentRequestDto.timelineId)
                .build();
    }

    @Override
    public String toString() {
        return "TimelineCommentRequestDto{" +
                "comment='" + comment + '\'' +
                ", userId=" + userId +
                ", timelineId=" + timelineId +
                '}';
    }
}
