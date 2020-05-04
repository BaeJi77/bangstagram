package com.bangstagram.timeline.service;


import com.bangstagram.timeline.domain.repository.TimelineRepository;
import com.bangstagram.timeline.dto.TimelineRequestDto;
import com.bangstagram.timeline.dto.TimelineResponseDto;
import com.bangstagram.timeline.dto.TimelineUpdateRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * author: Ji-Hoon Bae
 * Date: 2020.04.29
 */

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
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
    @DisplayName("(성공) 타임라인 만들기: 새로 만든 데이터와 기대값과 비교")
    public void isSuccessCreateNewTimeline() {
        // given => 데이터 셋업
        TimelineResponseDto expectedTimelineResponseDto
                = TimelineResponseDto.builder()
                .id(1L)
                .title("new")
                .body("new")
                .userId(1L)
                .roomId(1L)
                .createdAt(LocalDateTime.now())
                .build();

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
    @DisplayName("(성공) 타임라인 업데이트: 업데이트한 데이터와 기대값과 같은지 비교")
    public void isSuccessUpdateTimeline() {
        TimelineResponseDto expectedTimelineResponseDto
                = TimelineResponseDto.builder()
                .id(1L)
                .title("new")
                .body("new")
                .userId(1L)
                .roomId(1L)
                .createdAt(LocalDateTime.now())
                .build();

        TimelineRequestDto oldTimelineRequestDto
                = new TimelineRequestDto("old", "old", 1L, 1L);
        TimelineUpdateRequestDto updateRequestDto
                = new TimelineUpdateRequestDto("new", "new");

        TimelineResponseDto oldTimelineResponseDto = timelineService.createNewTimeline(oldTimelineRequestDto);
        TimelineResponseDto updatedTimelineResponseDto
                = timelineService.updateTimeline(oldTimelineResponseDto.getId(), updateRequestDto);

        assertThat(expectedTimelineResponseDto.getBody()).isEqualTo(updatedTimelineResponseDto.getBody());
        assertThat(expectedTimelineResponseDto.getTitle()).isEqualTo(updatedTimelineResponseDto.getTitle());
        assertThat(expectedTimelineResponseDto.getUserId()).isEqualTo(updatedTimelineResponseDto.getUserId());
        assertThat(expectedTimelineResponseDto.getRoomId()).isEqualTo(updatedTimelineResponseDto.getRoomId());
    }
}
