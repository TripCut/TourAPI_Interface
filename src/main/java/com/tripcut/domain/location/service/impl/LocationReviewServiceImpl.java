package com.tripcut.domain.location.service.impl;// package com.tripcut.domain.location.service.impl;

import com.tripcut.domain.filmingLocation.entity.FilmingLocation;
import com.tripcut.domain.filmingLocation.repository.FilmingLocationRepository;
import com.tripcut.domain.location.dto.*;
import com.tripcut.domain.location.entity.LocationReview;
import com.tripcut.domain.location.repository.LocationReviewRepository;
import com.tripcut.domain.location.service.LocationReviewService;
import com.tripcut.domain.user.entity.User;
import com.tripcut.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Transactional
public class LocationReviewServiceImpl implements LocationReviewService {

    private final LocationReviewRepository reviewRepository;
    private final FilmingLocationRepository locationRepository;
    private final UserRepository userRepository;

    @Override
    public LocationReviewDto create(Long locationId, Long authUserId, LocationReviewCreateRequest req) {
        FilmingLocation location = locationRepository.findById(locationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "촬영지를 찾을 수 없습니다. id=" + locationId));

        User user = userRepository.findById(authUserId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다. id=" + authUserId));

        LocationReview r = new LocationReview();
        r.setFilmingLocation(location);
        r.setUser(user);
        r.setTitle(req.getTitle());
        r.setContent(req.getContent());
        r.setRating(req.getRating());

        return toDto(reviewRepository.save(r));
    }

    @Transactional(readOnly = true)
    @Override
    public LocationReviewDto get(Long reviewId) {
        LocationReview r = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "리뷰가 없습니다. id=" + reviewId));
        return toDto(r);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<LocationReviewDto> listByLocation(Long locationId, Pageable pageable) {
        return reviewRepository.findByFilmingLocation_Id(locationId, pageable).map(this::toDto);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<LocationReviewDto> listMine(Long authUserId, Pageable pageable) {
        return reviewRepository.findByUser_Id(authUserId, pageable).map(this::toDto);
    }

    @Override
    public LocationReviewDto update(Long reviewId, Long authUserId, LocationReviewUpdateRequest req) {
        LocationReview r = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "리뷰가 없습니다. id=" + reviewId));

        if (!r.getUser().getId().equals(authUserId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "본인 리뷰만 수정할 수 있습니다.");
        }

        r.setTitle(req.getTitle());
        r.setContent(req.getContent());
        r.setRating(req.getRating());

        return toDto(r); // Dirty checking으로 갱신
    }

    @Override
    public void delete(Long reviewId, Long authUserId) {
        LocationReview r = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "리뷰가 없습니다. id=" + reviewId));

        if (!r.getUser().getId().equals(authUserId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "본인 리뷰만 삭제할 수 있습니다.");
        }
        reviewRepository.delete(r);
    }

    @Override
    public Double getAverageRating(Long locationId) {
        return reviewRepository.getAverageRatingByLocationId(locationId);
    }

    @Override
    public Double getReviewCount(Long locationId) {
        return reviewRepository.getReviewCountByLocationId(locationId);
    }

    @Override
    public Double calculateDistance(Double lat1, Double lon1, Double lat2, Double lon2) {
        final int R = 6371; // 지구의 반지름 (km)

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }

    private LocationReviewDto toDto(LocationReview r) {
        return LocationReviewDto.builder()
                .id(r.getId())
                .locationId(r.getFilmingLocation().getId())
                .userId(r.getUser().getId())
                .userUsername(r.getUser().getName()) // 프로젝트에 맞게 username/닉네임 필드로 교체 가능
                .title(r.getTitle())
                .content(r.getContent())
                .rating(r.getRating())
                .createdAt(r.getCreatedAt())
                .updatedAt(r.getUpdatedAt())
                .build();
    }
}
