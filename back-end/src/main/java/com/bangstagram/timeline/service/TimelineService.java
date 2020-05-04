package com.bangstagram.timeline.service;


import com.bangstagram.timeline.domain.model.Timeline;
import com.bangstagram.timeline.domain.repository.TimelineRepository;
import com.bangstagram.timeline.dto.TimelineRequestDto;
import com.bangstagram.timeline.dto.TimelineResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
public class TimelineService {
    private final TimelineRepository timelineRepository;

    public TimelineService(TimelineRepository timelineRepository) {
        this.timelineRepository = timelineRepository;
    }

    public TimelineResponseDto createNewTimeline(TimelineRequestDto timelineRequestDto) {
        log.info("{}", timelineRequestDto);
        Timeline newTimeline = timelineRepository.save(timelineRequestDto.convertToTimeline());
        return TimelineResponseDto.builder()
                .id(newTimeline.getId())
                .title(newTimeline.getTitle())
                .body(newTimeline.getBody())
                .userId(newTimeline.getUserId())
                .roomId(newTimeline.getRoomId())
                .createdAt(newTimeline.getCreatedAt())
                .build();
    }

    @Transactional(readOnly = true)
    public List<TimelineResponseDto> getTimelineByUserId(Long userId) {
        return timelineRepository.findByUserId(userId).stream()
                .map(timeline -> TimelineResponseDto.builder()
                        .id(timeline.getId())
                        .title(timeline.getTitle())
                        .body(timeline.getBody())
                        .createdAt(timeline.getCreatedAt())
                        .userId(timeline.getUserId())
                        .roomId(timeline.getRoomId())
                        .build())
                .collect(Collectors.toList());
    }
}
