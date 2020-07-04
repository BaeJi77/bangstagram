package com.bangstagram.timeline.controller.dto.request;


import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class TimelineCommentUpdateRequestDto {
    @NotBlank
    private String comment;

    public TimelineCommentUpdateRequestDto() {
    }

    @Builder
    public TimelineCommentUpdateRequestDto(@NotBlank String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "TimelineCommentUpdateRequestDto{" +
                "comment='" + comment + '\'' +
                '}';
    }
}
