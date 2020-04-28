package com.bangstagram.api.timeline.service;


import com.bangstagram.timeline.dto.TimelineRequestDto;
import com.bangstagram.timeline.dto.TimelineResponseDto;
import com.bangstagram.timeline.service.TimelineService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class TimelineServiceTest {
    @Autowired
    private TimelineService timelineService;

    @Test
    public void isSuccessCreateNewTimeline() {
        // given => 데이터 셋업
        TimelineResponseDto expectedTimelineResponseDto =
                new TimelineResponseDto(1L, "new", "new", LocalDateTime.now(), 1L, 1L);
        TimelineRequestDto timelineRequestDto
                = new TimelineRequestDto("new", "new", 1L, 1L);

        // when => 테스트하고 싶은 항목 실행
        TimelineResponseDto newTimelineResponseDto = timelineService.createNewTimeline(timelineRequestDto);

        // then => 비교
        assertThat(expectedTimelineResponseDto).isEqualTo(newTimelineResponseDto);
    }
}
