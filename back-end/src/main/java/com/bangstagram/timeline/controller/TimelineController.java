package com.bangstagram.timeline.controller;

import com.bangstagram.timeline.controller.dto.request.TimelineRequestDto;
import com.bangstagram.timeline.controller.dto.request.TimelineUpdateRequestDto;
import com.bangstagram.timeline.controller.dto.response.TimelineResponseDto;
import com.bangstagram.timeline.service.TimelineService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity createTimeline(@RequestBody @Valid TimelineRequestDto timelineRequestDto) {
        log.info("[Timeline]: {}", timelineRequestDto);
        TimelineResponseDto newTimeline = timelineService.createNewTimeline(timelineRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(newTimeline);
    }

    @PutMapping("/timelines/{timelineId}")
    public ResponseEntity updateTimeline(@PathVariable("timelineId") Long timelineId, @RequestBody @Valid TimelineUpdateRequestDto timelineUpdateRequestDto) {
        log.info("[Timeline]: {}", timelineUpdateRequestDto);
        TimelineResponseDto updatedResult = timelineService.updateTimeline(timelineId, timelineUpdateRequestDto);

        return ResponseEntity.status(HttpStatus.OK)
                .body(updatedResult);
    }
}