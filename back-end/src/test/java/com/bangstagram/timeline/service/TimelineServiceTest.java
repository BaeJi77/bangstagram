package com.bangstagram.api.timeline.service;


import com.bangstagram.timeline.domain.repository.TimelineRepository;
import com.bangstagram.timeline.dto.TimelineRequestDto;
import com.bangstagram.timeline.dto.TimelineResponseDto;
import com.bangstagram.timeline.service.TimelineService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

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

    @Test
    @DisplayName("(성공) 타임라인 가져오기: 특정 userId에 대하여 타임라인 얻기")
    public void isSuccessGetAllTimelineRelatedUserId() {
        long requestUserId = 1L;
        TimelineResponseDto expectedTimelineResponseDto =
                new TimelineResponseDto(1L, "foundTitle", "foundBody", LocalDateTime.now(), 1L, 1L);
        TimelineRequestDto timelineRequestDto
                = new TimelineRequestDto("foundTitle", "foundBody", 1L, 1L);

        timelineService.createNewTimeline(timelineRequestDto);
        List<TimelineResponseDto> foundResult = timelineService.getTimelineByUserId(requestUserId);

        assertThat(expectedTimelineResponseDto.getTitle()).isEqualTo(foundResult.get(0).getTitle());
        assertThat(expectedTimelineResponseDto.getBody()).isEqualTo(foundResult.get(0).getBody());
        assertThat(expectedTimelineResponseDto.getRoomId()).isEqualTo(foundResult.get(0).getRoomId());
        assertThat(expectedTimelineResponseDto.getUserId()).isEqualTo(foundResult.get(0).getUserId());
    }
}
