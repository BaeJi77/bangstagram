package com.bangstagram.timeline.service;


import com.bangstagram.common.exception.DoNotExistException;
import com.bangstagram.timeline.controller.dto.request.TimelineRequestDto;
import com.bangstagram.timeline.controller.dto.request.TimelineUpdateRequestDto;
import com.bangstagram.timeline.controller.dto.response.TimelineResponseDto;
import com.bangstagram.timeline.domain.model.Timeline;
import com.bangstagram.timeline.domain.repository.TimelineRepository;
import com.bangstagram.timeline.dto.TimelineRequestDto;
import com.bangstagram.timeline.dto.TimelineResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * author: Ji-Hoon Bae
 * Date: 2020.04.28
 */

@Service
@Slf4j
public class TimelineService {
    private final TimelineRepository timelineRepository;

    public TimelineService(TimelineRepository timelineRepository) {
        this.timelineRepository = timelineRepository;
    }

    public TimelineResponseDto createNewTimeline(TimelineRequestDto timelineRequestDto) {
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

    @Transactional
    public TimelineResponseDto updateTimeline(Long timelineId, TimelineUpdateRequestDto timelineUpdateRequestDto) {
        Timeline foundTimeline = timelineRepository.findById(timelineId)
                .orElseThrow(() -> new DoNotExistException("타임라인 정보를 찾을 수 없습니다."));

        foundTimeline.update(timelineUpdateRequestDto.getTitle(), timelineUpdateRequestDto.getBody());

        return TimelineResponseDto.builder()
                .id(foundTimeline.getId())
                .title(foundTimeline.getTitle())
                .body(foundTimeline.getBody())
                .createdAt(foundTimeline.getCreatedAt())
                .userId(foundTimeline.getUserId())
                .roomId(foundTimeline.getRoomId())
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
