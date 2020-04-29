package com.bangstagram.timeline.controller;

import com.bangstagram.timeline.dto.TimelineRequestDto;
import com.bangstagram.timeline.dto.TimelineResponseDto;
import com.bangstagram.timeline.dto.TimelineUpdateRequestDto;
import com.bangstagram.timeline.service.TimelineService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@Slf4j
public class TimelineController {
    private static final String TIMELINES = "/timelines";
    private final TimelineService timelineService;

    public TimelineController(TimelineService timelineService) {
        this.timelineService = timelineService;
    }

    @PostMapping(TIMELINES)
    @ResponseBody
    public ResponseEntity createTimeline (@RequestBody @Valid TimelineRequestDto timelineRequestDto) {
        TimelineResponseDto newTimeline = timelineService.createNewTimeline(timelineRequestDto);
        log.info("{}", newTimeline);
        return new ResponseEntity<>(newTimeline, HttpStatus.CREATED);
    }

    @PutMapping(TIMELINES + "/{timelineId}")
    @ResponseBody
    public ResponseEntity updateTimeline (@PathVariable("timelineId") Long timelineId, @RequestBody @Valid TimelineUpdateRequestDto timelineUpdateRequestDto) {
        TimelineResponseDto updatedResult = timelineService.updateTimeline(timelineId, timelineUpdateRequestDto);
        log.info("{}", updatedResult);
        return new ResponseEntity<>(updatedResult, HttpStatus.CREATED);
    }
}