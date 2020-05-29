package com.bangstagram.timeline.controller;

import com.bangstagram.timeline.controller.dto.request.TimelineRequestDto;
import com.bangstagram.timeline.controller.dto.request.TimelineUpdateRequestDto;
import com.bangstagram.timeline.controller.dto.response.TimelineResponseDto;
import com.bangstagram.timeline.service.TimelineService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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

    @GetMapping("/timelines/{id}")
    public List<TimelineResponseDto> getTimelineById(@PathVariable long id) {
        log.info("[get Timeline]: {}", id);
        return timelineService.getTimelineByUserId(id);
    }
}