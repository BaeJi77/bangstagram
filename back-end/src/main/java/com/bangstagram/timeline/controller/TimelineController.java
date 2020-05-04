package com.bangstagram.timeline.controller;

import com.bangstagram.timeline.dto.TimelineRequestDto;
import com.bangstagram.timeline.dto.TimelineResponseDto;
import com.bangstagram.timeline.service.TimelineService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
public class TimelineController {
    private static final String TIMELINES = "/timelines";

    private final TimelineService timelineService;

    public TimelineController(TimelineService timelineService) {
        this.timelineService = timelineService;
    }

    @PostMapping(TIMELINES)
    public ResponseEntity createTimeline(@RequestBody @Valid TimelineRequestDto timelineRequestDto) {
        TimelineResponseDto timelineResponseDto = timelineService.createNewTimeline(timelineRequestDto);
        log.info("{}", timelineResponseDto);
        return new ResponseEntity<>(timelineResponseDto, HttpStatus.CREATED);
    }

    @GetMapping(TIMELINES + "/{userId}")
    public ResponseEntity getTimelineByUserId(@PathVariable Long userId) {
        List<TimelineResponseDto> timelineResponseDtoList = timelineService.getTimelineByUserId(userId);
        return new ResponseEntity<>(timelineResponseDtoList, HttpStatus.OK);
    }
}