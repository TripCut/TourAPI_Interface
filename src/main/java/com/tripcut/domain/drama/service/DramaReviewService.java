package com.tripcut.domain.drama.service;

import com.tripcut.domain.drama.dto.DramaReviewCreateRequest;
import com.tripcut.domain.drama.dto.DramaReviewDto;
import com.tripcut.domain.drama.dto.DramaReviewUpdateRequest;
import com.tripcut.domain.drama.entity.DramaReview;

import java.util.List;


public interface DramaReviewService {
    DramaReview create(Long dramaId, Long userId, DramaReviewCreateRequest dramaReviewCreateRequest);

    DramaReview update(Long reviewId, Long userId, DramaReviewUpdateRequest dramaReviewUpdateRequest);

    List<DramaReviewDto> getAllReviews(Long dramaId);
    void delete(Long reviewId, Long userId);
}
