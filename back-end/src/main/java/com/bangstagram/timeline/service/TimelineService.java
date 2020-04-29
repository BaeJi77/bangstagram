package com.bangstagram.timeline.service;


import com.bangstagram.exception.DoNotExistException;
import com.bangstagram.timeline.domain.model.Timeline;
import com.bangstagram.timeline.domain.repository.TimelineRepository;
import com.bangstagram.timeline.dto.TimelineRequestDto;
import com.bangstagram.timeline.dto.TimelineResponseDto;
import com.bangstagram.timeline.dto.TimelineUpdateRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 author: Ji-Hoon Bae
 Date: 2020.04.28
 */

@Service
@Slf4j
public class TimelineService {
    private final TimelineRepository timelineRepository;

    public TimelineService(TimelineRepository timelineRepository) {
        this.timelineRepository = timelineRepository;
    }

    public TimelineResponseDto createNewTimeline (TimelineRequestDto timelineRequestDto) {
        log.info("{}", timelineRequestDto);
        Timeline newTimeline = timelineRepository.save(timelineRequestDto.convertTimelineRequestDtoToTimelineModel());
        return new TimelineResponseDto(newTimeline);
    }

    @Transactional
    public TimelineResponseDto updateTimeline (Long timelineId, TimelineUpdateRequestDto timelineUpdateRequestDto) {
        log.info("{} {}", timelineId, timelineUpdateRequestDto);
        Timeline foundTimeline = timelineRepository.findById(timelineId).orElseThrow(() -> new DoNotExistException("There is not target id in database. timelineId: ", timelineId));
        foundTimeline.update(timelineUpdateRequestDto.getTitle(), timelineUpdateRequestDto.getBody());
        return new TimelineResponseDto(foundTimeline);
    }
}
