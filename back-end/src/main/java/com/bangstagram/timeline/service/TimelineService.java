package com.bangstagram.timeline.service;


import com.bangstagram.common.exception.DoNotExistException;
import com.bangstagram.timeline.domain.model.Timeline;
import com.bangstagram.timeline.domain.repository.TimelineRepository;
import com.bangstagram.timeline.dto.TimelineRequestDto;
import com.bangstagram.timeline.dto.TimelineResponseDto;
import com.bangstagram.timeline.dto.TimelineUpdateRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
                .orElseThrow(() -> new DoNotExistException("요청하신 타임에 대한 정보를 찾을 수 없습니다. 해당 타임라인에 대하여 확인해주세요."));

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
}
