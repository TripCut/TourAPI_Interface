package com.tripcut.domain.drama.controller;

import com.tripcut.core.controller.BaseController;
import com.tripcut.domain.drama.dto.DramaReviewRequestDto;
import com.tripcut.domain.drama.dto.DramaReviewResponseDto;
import com.tripcut.domain.drama.service.DramaReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/drama-reviews")
@RequiredArgsConstructor
public class DramaReviewController extends BaseController {
    private final DramaReviewService reviewService;

    // 드라마별 리뷰 목록
    @GetMapping("/drama/{dramaId}")
    public Object getReviewsByDrama(@PathVariable Long dramaId) {
        List<DramaReviewResponseDto> result = reviewService.getReviewsByDrama(dramaId);
        return success(result);
    }

    // 유저별 리뷰 목록
    @GetMapping("/user/{userId}")
    public Object getReviewsByUser(@PathVariable Long userId) {
        List<DramaReviewResponseDto> result = reviewService.getReviewsByUser(userId);
        return success(result);
    }

    // 단일 리뷰 조회
    @GetMapping("/{reviewId}")
    public Object getReview(@PathVariable Long reviewId) {
        DramaReviewResponseDto dto = reviewService.getReview(reviewId);
        if (dto == null) return notFound();
        return success(dto);
    }

    // 리뷰 등록
    @PostMapping
    public Object createReview(@RequestBody DramaReviewRequestDto dto, @RequestParam Long userId) {
        DramaReviewResponseDto saved = reviewService.createReview(dto, userId);
        return success(saved);
    }

    // 리뷰 수정
    @PutMapping("/{reviewId}")
    public Object updateReview(@PathVariable Long reviewId, @RequestBody DramaReviewRequestDto dto, @RequestParam Long userId) {
        DramaReviewResponseDto updated = reviewService.updateReview(reviewId, dto, userId);
        return success(updated);
    }

    // 리뷰 삭제
    @DeleteMapping("/{reviewId}")
    public Object deleteReview(@PathVariable Long reviewId, @RequestParam Long userId) {
        reviewService.deleteReview(reviewId, userId);
        return success(null);
    }
} 