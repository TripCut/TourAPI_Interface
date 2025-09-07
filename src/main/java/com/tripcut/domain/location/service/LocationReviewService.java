package com.tripcut.domain.location.service;

import com.tripcut.core.annotation.Metrics;
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

    Double getAverageRating(Long locationId);

    @Metrics(name = "")
    Integer getReviewCount(Long locationId);

    Double calculateDistance(Double lat1, Double lon1, Double lat2, Double lon2);
}
