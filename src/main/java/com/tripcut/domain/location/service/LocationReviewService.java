package com.tripcut.domain.location.service;

import com.tripcut.domain.location.dto.LocationReviewCreateRequest;
import com.tripcut.domain.location.dto.LocationReviewDto;
import com.tripcut.domain.location.dto.LocationReviewUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LocationReviewService {
    LocationReviewDto create(Long locationId, Long authUserId, LocationReviewCreateRequest req);
    LocationReviewDto get(Long reviewId);
    Page<LocationReviewDto> listByLocation(Long locationId, Pageable pageable);
    Page<LocationReviewDto> listMine(Long authUserId, Pageable pageable);
    LocationReviewDto update(Long reviewId, Long authUserId, LocationReviewUpdateRequest req);
    void delete(Long reviewId, Long authUserId);
}
