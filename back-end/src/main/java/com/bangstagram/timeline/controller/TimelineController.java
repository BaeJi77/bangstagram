package com.bangstagram.timeline.controller;

import com.bangstagram.timeline.dto.TimelineRequestDto;
import com.bangstagram.timeline.dto.TimelineResponseDto;
import com.bangstagram.timeline.service.TimelineService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Slf4j
public class TimelineController {
    private static final String TIMELINES = "/timelines";

    private final TimelineService timelineService;

    public TimelineController(TimelineService timelineService) {
        this.timelineService = timelineService;
    }

    @PostMapping(TIMELINES)
    public ResponseEntity createTimeline (@RequestBody @Valid TimelineRequestDto timelineRequestDto) {
        TimelineResponseDto timelineResponseDto = timelineService.createNewTimeline(timelineRequestDto);
        log.info("{}", timelineResponseDto);
        return new ResponseEntity<>(timelineResponseDto, HttpStatus.CREATED);
    }
}