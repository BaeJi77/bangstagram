package com.bangstagram.room.service;

import com.bangstagram.room.controller.dto.request.ReviewSaveRequestDto;
import com.bangstagram.room.controller.dto.request.ReviewUpdateRequestDto;
import com.bangstagram.room.controller.dto.response.ReviewResponseDto;
import com.bangstagram.room.domain.model.Review;
import com.bangstagram.room.domain.model.Theme;
import com.bangstagram.room.domain.repository.ReviewRepository;
import com.bangstagram.room.domain.repository.RoomRepository;
import com.bangstagram.room.domain.repository.ThemeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ReviewServiceTest {
    @Autowired
    private ReviewService reviewService;

    @Autowired
    private ThemeRepository themeRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private RoomRepository roomRepository;

    @BeforeEach
    void setUp() {
        reviewRepository.deleteAll();
        themeRepository.deleteAll();
    }

    @Test
    void findByThemeId() {
        // given
        Theme newTheme = Theme.builder()
                .title("theme title")
                .description("theme desc")
                .genre("theme genre")
                .imgSrc("src")
                .build();
        Theme savedTheme = themeRepository.save(newTheme);

        Review newReview = Review.builder()
                .userId(1L)
                .content("review")
                .level("level")
                .hintCount(1)
                .score(100)
                .leftTime(30)
                .success(true)
                .build();
        newReview.setTheme(savedTheme);
        Review savedReview = reviewRepository.save(newReview);

        // when
        List<ReviewResponseDto> responseDtos = reviewService.findByThemeId(1L, savedTheme.getId());

        // then
        assertThat(responseDtos.get(0).getContent()).isEqualTo(savedReview.getContent());
        assertThat(responseDtos.get(0).getUserId()).isEqualTo(savedReview.getUserId());
        assertThat(responseDtos.get(0).getThemeId()).isEqualTo(savedReview.getTheme().getId());
        assertThat(responseDtos.get(0).getHintCount()).isEqualTo(savedReview.getHintCount());
        assertThat(responseDtos.get(0).getLevel()).isEqualTo(savedReview.getLevel());
        assertThat(responseDtos.get(0).getScore()).isEqualTo(savedReview.getScore());
        assertThat(responseDtos.get(0).isSuccess()).isEqualTo(savedReview.isSuccess());
    }

    @Test
    void createReview() {
        // given
        Theme newTheme = Theme.builder()
                .title("newTheme")
                .description("hi")
                .genre("fun")
                .imgSrc("https:1111")
                .build();
        Theme savedTheme = themeRepository.save(newTheme);

        ReviewSaveRequestDto saveRequestDto = ReviewSaveRequestDto.builder()
                .userId(1L)
                .themeId(savedTheme.getId())
                .content("new")
                .hintCount(3)
                .level("어려움")
                .leftTime(30)
                .score(5)
                .success(true)
                .build();

        // when
        ReviewResponseDto reviewResponseDto = reviewService.createReview(1L, savedTheme.getId(), saveRequestDto);

        // then
        assertThat(saveRequestDto.getUserId()).isEqualTo(reviewResponseDto.getUserId());
        assertThat(saveRequestDto.getThemeId()).isEqualTo(reviewResponseDto.getThemeId());
        assertThat(saveRequestDto.getContent()).isEqualTo(reviewResponseDto.getContent());
        assertThat(saveRequestDto.getHintCount()).isEqualTo(reviewResponseDto.getHintCount());
        assertThat(saveRequestDto.getLevel()).isEqualTo(reviewResponseDto.getLevel());
        assertThat(saveRequestDto.getScore()).isEqualTo(reviewResponseDto.getScore());
        assertThat(saveRequestDto.isSuccess()).isEqualTo(reviewResponseDto.isSuccess());
    }

    @Test
    void updateReview() {
        // given
        Theme theme = Theme.builder()
                .title("theme")
                .genre("genre")
                .imgSrc("src")
                .description("desc")
                .build();
        Theme savedTheme = themeRepository.save(theme);

        Review review = Review.builder()
                .userId(1L)
                .content("review")
                .score(100)
                .hintCount(1)
                .leftTime(10)
                .level("level")
                .success(true)
                .build();
        review.setTheme(savedTheme);
        Review savedReview = reviewRepository.save(review);

        ReviewUpdateRequestDto updateRequestDto = ReviewUpdateRequestDto.builder()
                .content("updateReview")
                .score(80)
                .hintCount(5)
                .leftTime(30)
                .level("newLevel")
                .success(false)
                .build();

        // when
        ReviewResponseDto responseDto = reviewService.updateReview(1L, savedTheme.getId(), savedReview.getId(), updateRequestDto);

        // then
        assertThat(responseDto.getContent()).isEqualTo(updateRequestDto.getContent());
        assertThat(responseDto.getScore()).isEqualTo(updateRequestDto.getScore());
        assertThat(responseDto.getHintCount()).isEqualTo(updateRequestDto.getHintCount());
        assertThat(responseDto.getLeftTime()).isEqualTo(updateRequestDto.getLeftTime());
        assertThat(responseDto.getLevel()).isEqualTo(updateRequestDto.getLevel());
        assertThat(responseDto.isSuccess()).isEqualTo(updateRequestDto.isSuccess());
    }
}