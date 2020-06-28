package com.bangstagram.timeline.service;

import com.bangstagram.common.exception.DoNotExistException;
import com.bangstagram.timeline.controller.dto.request.TimelineCommentRequestDto;
import com.bangstagram.timeline.controller.dto.request.TimelineCommentUpdateRequestDto;
import com.bangstagram.timeline.controller.dto.response.TimelineCommentResponseDto;
import com.bangstagram.timeline.domain.model.Timeline;
import com.bangstagram.timeline.domain.model.TimelineComment;
import com.bangstagram.timeline.domain.repository.TimelineCommentRepository;
import com.bangstagram.timeline.domain.repository.TimelineRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * author: Ji-Hoon Bae
 * Date: 2020.05.21
 */

@Service
public class TimelineCommentService {
    public final TimelineCommentRepository timelineCommentRepository;
    public final TimelineRepository timelineRepository;

    public TimelineCommentService(TimelineCommentRepository timelineCommentRepository, TimelineRepository timelineRepository) {
        this.timelineCommentRepository = timelineCommentRepository;
        this.timelineRepository = timelineRepository;
    }

    @Transactional
    public TimelineCommentResponseDto makeTimelineComment(TimelineCommentRequestDto timelineCommentRequestDto) {
        Timeline timeline = timelineRepository.findById(timelineCommentRequestDto.getTimelineId())
                .orElseThrow(() -> new DoNotExistException("해당 타임라인 정보가 없습니다."));

        TimelineComment newTimelineComment = timelineCommentRepository.save(TimelineCommentRequestDto.convertToTimelineComment(timelineCommentRequestDto));
        timeline.addTimelineComment(newTimelineComment);

        return TimelineCommentResponseDto.builder()
                .id(newTimelineComment.getId())
                .comment(newTimelineComment.getComment())
                .userId(newTimelineComment.getUserId())
                .timelineId(newTimelineComment.getTimelineId())
                .build();
    }

    @Transactional
    public TimelineCommentResponseDto updateComment(long timelineCommentId, TimelineCommentUpdateRequestDto timelineCommentUpdateRequestDto) {
        TimelineComment timelineComment = timelineCommentRepository.findById(timelineCommentId)
                .orElseThrow(() -> new DoNotExistException("해당 타임라인 커멘트 정보가 없습니다."));

        timelineComment.update(timelineCommentUpdateRequestDto.getComment());

        return TimelineCommentResponseDto.builder()
                .id(timelineComment.getId())
                .comment(timelineComment.getComment())
                .timelineId(timelineComment.getUserId())
                .userId(timelineComment.getUserId())
                .build();
    }
}