package com.bangstagram.timeline.controller;

import com.bangstagram.timeline.controller.dto.request.TimelineCommentRequestDto;
import com.bangstagram.timeline.controller.dto.response.TimelineCommentResponseDto;
import com.bangstagram.timeline.service.TimelineCommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * author: Ji-Hoon Bae
 * Date: 2020.05.21
 */

@RestController
@Slf4j
public class TimelineCommentController {
    public final TimelineCommentService timelineCommentService;

    public TimelineCommentController(TimelineCommentService timelineCommentService) {
        this.timelineCommentService = timelineCommentService;
    }

    @PostMapping("/timelines/comments")
    public TimelineCommentResponseDto createNewTimelineComment(@RequestBody @Valid TimelineCommentRequestDto timelineCommentRequestDto) {
        log.info("[TimelineComment] {}", timelineCommentRequestDto);
        return timelineCommentService.makeTimelineComment(timelineCommentRequestDto);
    }
}

