package com.bangstagram.timeline.service;


import com.bangstagram.timeline.domain.repository.TimelineRepository;
import com.bangstagram.timeline.dto.TimelineRequestDto;
import com.bangstagram.timeline.dto.TimelineResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

/**
 author: Ji-Hoon Bae
 Date: 2020.04.29
 */

@SpringBootTest
public class TimelineServiceTest {
    @Autowired
    private TimelineService timelineService;

    @Autowired
    private TimelineRepository timelineRepository;

    @BeforeEach
    public void setUp() {
        timelineRepository.deleteAll();
    }

    @Test
    @DisplayName("(성공) 타임라인 만들기: DB 접속 후 데이터와 비교하기")
    public void isSuccessCreateNewTimeline() {
        // given => 데이터 셋업
        TimelineResponseDto expectedTimelineResponseDto =
                new TimelineResponseDto(1L, "new", "new", LocalDateTime.now(), 1L, 1L);
        TimelineRequestDto timelineRequestDto
                = new TimelineRequestDto("new", "new", 1L, 1L);

        // when => 테스트하고 싶은 항목 실행
        TimelineResponseDto newTimelineResponseDto = timelineService.createNewTimeline(timelineRequestDto);

        // then => 비교
        assertThat(expectedTimelineResponseDto.getBody()).isEqualTo(newTimelineResponseDto.getBody());
        assertThat(expectedTimelineResponseDto.getTitle()).isEqualTo(newTimelineResponseDto.getTitle());
        assertThat(expectedTimelineResponseDto.getUserId()).isEqualTo(newTimelineResponseDto.getUserId());
        assertThat(expectedTimelineResponseDto.getRoomId()).isEqualTo(newTimelineResponseDto.getRoomId());
    }
}
