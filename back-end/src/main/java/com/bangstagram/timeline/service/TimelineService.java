package com.bangstagram.timeline.service;


import com.bangstagram.common.exception.DoNotExistException;
import com.bangstagram.timeline.controller.dto.request.TimelineRequestDto;
import com.bangstagram.timeline.controller.dto.request.TimelineUpdateRequestDto;
import com.bangstagram.timeline.controller.dto.response.TimelineResponseDto;
import com.bangstagram.timeline.domain.model.Timeline;
import com.bangstagram.timeline.domain.repository.TimelineRepository;
import com.bangstagram.user.domain.model.user.User;
import com.bangstagram.user.domain.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

/**
 * author: Ji-Hoon Bae
 * Date: 2020.04.28
 */

@Service
@Slf4j
public class TimelineService {
    private final TimelineRepository timelineRepository;

    private final UserRepository userRepository;

    public TimelineService(TimelineRepository timelineRepository, UserRepository userRepository) {
        this.timelineRepository = timelineRepository;
        this.userRepository = userRepository;
    }

    public TimelineResponseDto createNewTimeline(TimelineRequestDto timelineRequestDto, Long userId) {
        Timeline toSaveTimeline = timelineRequestDto.convertToTimeline();

        User user = userRepository.findById(userId).orElseThrow(() -> new DoNotExistException("사용자 정보를 찾을 수 없습니다."));
        toSaveTimeline.changeUser(user);

        Timeline newTimeline = timelineRepository.save(toSaveTimeline);

        return TimelineResponseDto.builder()
                .id(newTimeline.getId())
                .title(newTimeline.getTitle())
                .body(newTimeline.getBody())
                //.userId(foundTimeline.getUserId())
                .user(newTimeline.getUser())
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
                //.userId(foundTimeline.getUserId())
                .user(foundTimeline.getUser())
                .roomId(foundTimeline.getRoomId())
                .build();
    }

    @Transactional(readOnly = true)
    public List<TimelineResponseDto> getTimelineByUserId(Long userId) {
        return timelineRepository.findAllByUserId(userId).stream()
                .map(timeline -> TimelineResponseDto.builder()
                        .id(timeline.getId())
                        .title(timeline.getTitle())
                        .body(timeline.getBody())
                        .createdAt(timeline.getCreatedAt())
                        //.userId(timeline.getUserId())
                        .user(timeline.getUser())
                        .roomId(timeline.getRoomId())
                        .build())
                .collect(collectingAndThen(toList(), Collections::unmodifiableList)); // 불변성이 존재하는 리스트를 만든다
    }
}
