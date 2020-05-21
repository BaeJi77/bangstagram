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

/**
 * author: Ji-Hoon Bae
 * Date: 2020.04.28
 */

@RestController
@Slf4j
public class TimelineController {
    private final TimelineService timelineService;

    public TimelineController(TimelineService timelineService) {
        this.timelineService = timelineService;
    }

    @PostMapping("/timelines")
    public TimelineResponseDto createTimeline(@RequestBody @Valid TimelineRequestDto timelineRequestDto) {
        log.info("[create Timeline]: {}", timelineRequestDto);
        return timelineService.createNewTimeline(timelineRequestDto);
    }

    @PutMapping("/timelines/{id}")
    public TimelineResponseDto updateTimeline(@PathVariable("id") long id, @RequestBody @Valid TimelineUpdateRequestDto timelineUpdateRequestDto) {
        log.info("[update Timeline]: {}", timelineUpdateRequestDto);
        return timelineService.updateTimeline(id, timelineUpdateRequestDto);
    }

    @GetMapping(TIMELINES + "/{userId}")
    public ResponseEntity getTimelineByUserId(@PathVariable Long userId) {
        List<TimelineResponseDto> timelineResponseDtoList = timelineService.getTimelineByUserId(userId);
        return new ResponseEntity<>(timelineResponseDtoList, HttpStatus.OK);
    }
}