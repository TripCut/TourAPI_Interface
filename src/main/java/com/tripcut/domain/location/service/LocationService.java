package com.tripcut.domain.location.service;

import com.tripcut.domain.location.dto.FilmingLocationDto;
import com.tripcut.domain.location.dto.LocationReviewDto;

import java.util.List;

public interface LocationService {
    
    // 촬영지 CRUD
    FilmingLocationDto createLocation(FilmingLocationDto locationDto);
    FilmingLocationDto getLocationById(Long id);
    List<FilmingLocationDto> getAllLocations();
    FilmingLocationDto updateLocation(Long id, FilmingLocationDto locationDto);
    void deleteLocation(Long id);
    
    // 위치 기반 검색
    List<FilmingLocationDto> searchLocationsByKeyword(String keyword);
    List<FilmingLocationDto> searchLocationsByAddress(String address);
    List<FilmingLocationDto> findLocationsWithinRadius(Double latitude, Double longitude, Double radius);
    List<FilmingLocationDto> findLocationsByTags(List<String> tags);
    List<FilmingLocationDto> getLocationsByDramaId(Long dramaId);
    List<FilmingLocationDto> getTopRatedLocations(int limit);
    
    // 리뷰 관리
    LocationReviewDto createReview(LocationReviewDto reviewDto);
    List<LocationReviewDto> getReviewsByLocationId(Long locationId);
    LocationReviewDto updateReview(Long reviewId, LocationReviewDto reviewDto);
    void deleteReview(Long reviewId);
    
    // 통계
    Double getAverageRating(Long locationId);
    Integer getReviewCount(Long locationId);
    
    // 거리 계산
    Double calculateDistance(Double lat1, Double lon1, Double lat2, Double lon2);
}
