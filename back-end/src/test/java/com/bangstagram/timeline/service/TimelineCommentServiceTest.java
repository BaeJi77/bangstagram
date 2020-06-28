package com.bangstagram.timeline.service;


import com.bangstagram.timeline.controller.dto.request.TimelineCommentRequestDto;
import com.bangstagram.timeline.controller.dto.request.TimelineCommentUpdateRequestDto;
import com.bangstagram.timeline.controller.dto.response.TimelineCommentResponseDto;
import com.bangstagram.timeline.domain.model.Timeline;
import com.bangstagram.timeline.domain.repository.TimelineCommentRepository;
import com.bangstagram.timeline.domain.repository.TimelineRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * author: Ji-Hoon Bae
 * Date: 2020.06.28
 */

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TimelineCommentServiceTest {
    @Autowired
    private TimelineCommentService timelineCommentService;

    @Autowired
    private TimelineRepository timelineRepository;

    @Autowired
    private TimelineCommentRepository timelineCommentRepository;

    @BeforeEach
    public void setUp() {
        timelineRepository.deleteAll();
        timelineCommentRepository.deleteAll();
    }

    @Test
    @DisplayName("(성공) 타임라인 코멘트 만들기: 새로 만든 데이터와 기대값과 비교")
    public void isSuccessCreateNewTimeline() {
        // given => 데이터 셋업
        Timeline timeline = Timeline.builder().build();
        Timeline saveTimeline = timelineRepository.save(timeline);

        TimelineCommentResponseDto expectedTimelineCommentResponseDto
                = TimelineCommentResponseDto.builder()
                .id(1L)
                .comment("newComment")
                .userId(1L)
                .timelineId(saveTimeline.getId())
                .build();

        TimelineCommentRequestDto timelineCommentRequestDto
                = new TimelineCommentRequestDto("newComment", 1L, saveTimeline.getId());

        // when => 테스트하고 싶은 항목 실행
        TimelineCommentResponseDto newTimelineCommentResponseDto = timelineCommentService.makeTimelineComment(timelineCommentRequestDto);

        // then => 비교
        assertThat(expectedTimelineCommentResponseDto.getComment()).isEqualTo(newTimelineCommentResponseDto.getComment());
        assertThat(expectedTimelineCommentResponseDto.getTimelineId()).isEqualTo(newTimelineCommentResponseDto.getTimelineId());
        assertThat(expectedTimelineCommentResponseDto.getUserId()).isEqualTo(newTimelineCommentResponseDto.getUserId());
    }

    @Test
    @DisplayName("(성공) 타임라인 코멘트 업데이트: 업데이트한 데이터와 기대값과 같은지 비교")
    public void isSuccessUpdateTimeline() {
        // given => 데이터 셋업
        Timeline timeline = Timeline.builder().build();
        Timeline saveTimeline = timelineRepository.save(timeline);

        TimelineCommentUpdateRequestDto expectedTimelineCommentResponseDto = TimelineCommentUpdateRequestDto.builder()
                .comment("updateComment")
                .build();

        TimelineCommentRequestDto oldTimelineCommentRequestDto
                = new TimelineCommentRequestDto("oldComment", 1L, saveTimeline.getId());
        TimelineCommentUpdateRequestDto updateRequestDto
                = new TimelineCommentUpdateRequestDto("updateComment");

        // when => 테스트하고 싶은 항목 실행
        TimelineCommentResponseDto makeTimelineComment = timelineCommentService.makeTimelineComment(oldTimelineCommentRequestDto);
        TimelineCommentResponseDto updateComment = timelineCommentService.updateComment(makeTimelineComment.getId(), updateRequestDto);

        // then => 비교
        assertThat(expectedTimelineCommentResponseDto.getComment()).isEqualTo(updateComment.getComment());
    }
}
