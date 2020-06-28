package com.bangstagram.room.controller;

import com.bangstagram.room.controller.dto.request.ReviewSaveRequestDto;
import com.bangstagram.room.controller.dto.request.ReviewUpdateRequestDto;
import com.bangstagram.room.controller.dto.response.ReviewResponseDto;
import com.bangstagram.room.service.ReviewService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class ReviewController {
    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    // Room > Theme > Review
    @GetMapping("/rooms/{roomId}/themes/{themeId}/reviews")
    public List<ReviewResponseDto> findReviewsByThemeId(@PathVariable Long roomId, @PathVariable Long themeId) {
        return reviewService.findByThemeId(roomId, themeId);
    }

    @PostMapping("/rooms/{roomId}/themes/{themeId}/reviews")
    public ReviewResponseDto createReview(@PathVariable Long roomId, @PathVariable Long themeId, @RequestBody @Valid ReviewSaveRequestDto requestDto) {
        return reviewService.createReview(roomId, themeId, requestDto);
    }

    @PutMapping("/rooms/{roomId}/themes/{themeId}/reviews/{reviewId}")
    public ReviewResponseDto updateReview(@PathVariable Long roomId, @PathVariable Long themeId, @PathVariable Long reviewId, @RequestBody @Valid ReviewUpdateRequestDto requestDto) {
        return reviewService.updateReview(roomId, themeId, reviewId, requestDto);
    }

    @DeleteMapping("/rooms/{roomId}/themes/{themeId}/reviews/{reviewId}")
    public void deleteReview(@PathVariable Long roomId, @PathVariable Long themeId, @PathVariable Long reviewId) {
        reviewService.deleteReview(roomId, themeId, reviewId);
    }
}
