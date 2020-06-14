package com.bangstagram.room.service;

import com.bangstagram.common.exception.DoNotExistException;
import com.bangstagram.room.controller.dto.request.ReviewSaveRequestDto;
import com.bangstagram.room.controller.dto.request.ReviewUpdateRequestDto;
import com.bangstagram.room.controller.dto.response.ReviewResponseDto;
import com.bangstagram.room.domain.model.Review;
import com.bangstagram.room.domain.model.Theme;
import com.bangstagram.room.domain.repository.ReviewRepository;
import com.bangstagram.room.domain.repository.ThemeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewService {
    private final ThemeRepository themeRepository;
    private final ReviewRepository reviewRepository;

    public ReviewService(ThemeRepository themeRepository, ReviewRepository reviewRepository) {
        this.themeRepository = themeRepository;
        this.reviewRepository = reviewRepository;
    }

    @Transactional(readOnly = true)
    public List<ReviewResponseDto> findByThemeId(Long roomId, Long themeId) {
        return reviewRepository.findByThemeId(themeId).stream().map(review -> ReviewResponseDto.builder()
                .id(review.getId())
                .userId(review.getUserId())
                .themeId(review.getTheme().getId())
                .content(review.getContent())
                .score(review.getScore())
                .level(review.getLevel())
                .hintCount(review.getHintCount())
                .success(review.isSuccess())
                .leftTime(review.getLeftTime())
                .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public ReviewResponseDto createReview(Long roomId, Long themeId, ReviewSaveRequestDto requestDto) {
        Theme theme = themeRepository.findById(themeId).orElseThrow(() -> new DoNotExistException("테마 정보를 찾을 수 없습니다."));
        Review newReview = reviewRepository.save(requestDto.toEntity());

        newReview.setTheme(theme);

        return ReviewResponseDto.builder()
                .id(newReview.getId())
                .content(newReview.getContent())
                .userId(newReview.getUserId())
                .themeId(newReview.getTheme().getId())
                .leftTime(newReview.getLeftTime())
                .success(newReview.isSuccess())
                .hintCount(newReview.getHintCount())
                .level(newReview.getLevel())
                .score(newReview.getScore())
                .build();
    }

    @Transactional
    public ReviewResponseDto updateReview(Long roomId, Long themeId, Long reviewId, ReviewUpdateRequestDto requestDto) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new DoNotExistException("리뷰 정보를 찾을 수 없습니다."));
        review.update(requestDto.getContent(), requestDto.getScore(), requestDto.getLevel(), requestDto.isSuccess(), requestDto.getLeftTime(), requestDto.getHintCount());

        return ReviewResponseDto.builder()
                .id(review.getId())
                .userId(review.getUserId())
                .themeId(review.getTheme().getId())
                .content(review.getContent())
                .score(review.getScore())
                .level(review.getLevel())
                .success(review.isSuccess())
                .leftTime(review.getLeftTime())
                .hintCount(review.getHintCount())
                .build();
    }

    public void deleteReview(Long roomId, Long themeId, Long reviewId) {
        reviewRepository.deleteById(reviewId);
    }

}
