package com.tripcut.domain.drama.service;

import com.tripcut.domain.drama.dto.DramaReviewRequestDto;
import com.tripcut.domain.drama.dto.DramaReviewResponseDto;

import java.util.List;

public interface DramaReviewService {
    DramaReviewResponseDto createReview(DramaReviewRequestDto dto, Long userId);
    DramaReviewResponseDto updateReview(Long reviewId, DramaReviewRequestDto dto, Long userId);
    void deleteReview(Long reviewId, Long userId);
    DramaReviewResponseDto getReview(Long reviewId);
    List<DramaReviewResponseDto> getReviewsByDrama(Long dramaId);
    List<DramaReviewResponseDto> getReviewsByUser(Long userId);
} 