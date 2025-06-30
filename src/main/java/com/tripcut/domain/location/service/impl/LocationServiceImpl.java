package com.tripcut.domain.location.service.impl;

import com.tripcut.core.annotation.Logging;
import com.tripcut.core.annotation.Metrics;
import com.tripcut.core.annotation.TourAPI;
import com.tripcut.domain.drama.entity.Drama;
import com.tripcut.domain.drama.repository.DramaRepository;
import com.tripcut.domain.location.dto.FilmingLocationDto;
import com.tripcut.domain.location.dto.LocationReviewDto;
import com.tripcut.domain.location.entity.FilmingLocation;
import com.tripcut.domain.location.entity.LocationReview;
import com.tripcut.domain.location.repository.FilmingLocationRepository;
import com.tripcut.domain.location.repository.LocationReviewRepository;
import com.tripcut.domain.location.service.LocationService;
import com.tripcut.domain.user.entity.User;
import com.tripcut.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class LocationServiceImpl implements LocationService {

    private final FilmingLocationRepository locationRepository;
    private final LocationReviewRepository reviewRepository;
    private final DramaRepository dramaRepository;
    private final UserRepository userRepository;

    @Override
    @Logging
    @Metrics(name = "")
    @TourAPI(service = "location", operation = "create")
    public FilmingLocationDto createLocation(FilmingLocationDto locationDto) {
        Drama drama = dramaRepository.findById(locationDto.getDramaId())
                .orElseThrow(() -> new RuntimeException("Drama not found with id: " + locationDto.getDramaId()));
        
        FilmingLocation location = new FilmingLocation();
        location.setName(locationDto.getName());
        location.setAddress(locationDto.getAddress());
        location.setDescription(locationDto.getDescription());
        location.setSceneDescription(locationDto.getSceneDescription());
        location.setLatitude(locationDto.getLatitude());
        location.setLongitude(locationDto.getLongitude());
        location.setDrama(drama);
        location.setImages(locationDto.getImages());
        location.setTags(locationDto.getTags());
        
        FilmingLocation savedLocation = locationRepository.save(location);
        return convertToDto(savedLocation);
    }

    @Override
    @Logging
    @Metrics(name = "")
    @TourAPI(service = "location", operation = "detail")
    public FilmingLocationDto getLocationById(Long id) {
        FilmingLocation location = locationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Location not found with id: " + id));
        return convertToDto(location);
    }

    @Override
    @Logging
    @Metrics(name = "")
    @TourAPI(service = "location", operation = "list")
    public List<FilmingLocationDto> getAllLocations() {
        return locationRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Logging
    @Metrics(name = "")
    @TourAPI(service = "location", operation = "update")
    public FilmingLocationDto updateLocation(Long id, FilmingLocationDto locationDto) {
        FilmingLocation location = locationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Location not found with id: " + id));
        
        location.setName(locationDto.getName());
        location.setAddress(locationDto.getAddress());
        location.setDescription(locationDto.getDescription());
        location.setSceneDescription(locationDto.getSceneDescription());
        location.setLatitude(locationDto.getLatitude());
        location.setLongitude(locationDto.getLongitude());
        location.setImages(locationDto.getImages());
        location.setTags(locationDto.getTags());
        
        FilmingLocation updatedLocation = locationRepository.save(location);
        return convertToDto(updatedLocation);
    }

    @Override
    @Logging
    @Metrics(name = "")
    @TourAPI(service = "location", operation = "delete")
    public void deleteLocation(Long id) {
        if (!locationRepository.existsById(id)) {
            throw new RuntimeException("Location not found with id: " + id);
        }
        locationRepository.deleteById(id);
    }

    @Override
    @Logging
    @Metrics(name = "")
    @TourAPI(service = "location", operation = "search")
    public List<FilmingLocationDto> searchLocationsByKeyword(String keyword) {
        return locationRepository.findByKeyword(keyword).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Logging
    @Metrics(name = "")
    public List<FilmingLocationDto> searchLocationsByAddress(String address) {
        return locationRepository.findByAddressContaining(address).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Logging
    @Metrics(name = "")
    public List<FilmingLocationDto> findLocationsWithinRadius(Double latitude, Double longitude, Double radius) {
        return locationRepository.findByLocationWithinRadius(latitude, longitude, radius).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Logging
    @Metrics(name = "")
    public List<FilmingLocationDto> findLocationsByTags(List<String> tags) {
        return locationRepository.findByTags(tags).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Logging
    @Metrics(name = "")
    public List<FilmingLocationDto> getLocationsByDramaId(Long dramaId) {
        return locationRepository.findByDramaId(dramaId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Logging
    @Metrics(name = "")
    public List<FilmingLocationDto> getTopRatedLocations(int limit) {
        return locationRepository.findAllOrderByAverageRating().stream()
                .limit(limit)
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Logging
    @Metrics(name = "")
    public LocationReviewDto createReview(LocationReviewDto reviewDto) {
        FilmingLocation location = locationRepository.findById(reviewDto.getLocationId())
                .orElseThrow(() -> new RuntimeException("Location not found with id: " + reviewDto.getLocationId()));
        
        User user = userRepository.findById(reviewDto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + reviewDto.getUserId()));
        
        LocationReview review = new LocationReview();
        review.setFilmingLocation(location);
        review.setUser(user);
        review.setTitle(reviewDto.getTitle());
        review.setContent(reviewDto.getContent());
        review.setRating(reviewDto.getRating());
        
        LocationReview savedReview = reviewRepository.save(review);
        return convertToReviewDto(savedReview);
    }

    @Override
    @Logging
    @Metrics(name = "")
    public List<LocationReviewDto> getReviewsByLocationId(Long locationId) {
        return reviewRepository.findByFilmingLocationId(locationId).stream()
                .map(this::convertToReviewDto)
                .collect(Collectors.toList());
    }

    @Override
    @Logging
    @Metrics(name = "")
    public LocationReviewDto updateReview(Long reviewId, LocationReviewDto reviewDto) {
        LocationReview review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found with id: " + reviewId));
        
        review.setTitle(reviewDto.getTitle());
        review.setContent(reviewDto.getContent());
        review.setRating(reviewDto.getRating());
        
        LocationReview updatedReview = reviewRepository.save(review);
        return convertToReviewDto(updatedReview);
    }

    @Override
    @Logging
    @Metrics(name = "")
    public void deleteReview(Long reviewId) {
        if (!reviewRepository.existsById(reviewId)) {
            throw new RuntimeException("Review not found with id: " + reviewId);
        }
        reviewRepository.deleteById(reviewId);
    }

    @Override
    @Logging
    @Metrics(name = "")
    public Double getAverageRating(Long locationId) {
        return reviewRepository.getAverageRatingByLocationId(locationId);
    }

    @Override
    @Logging
    @Metrics(name = "")
    public Integer getReviewCount(Long locationId) {
        return reviewRepository.getReviewCountByLocationId(locationId);
    }

    @Override
    @Logging
    @Metrics(name = "")
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

    // Function 로직
    private FilmingLocationDto convertToDto(FilmingLocation location) {
        Double averageRating = getAverageRating(location.getId());
        Integer reviewCount = getReviewCount(location.getId());
        
        List<String> stampTypes = location.getStamps().stream()
                .map(stamp -> stamp.getStampType())
                .distinct()
                .collect(Collectors.toList());
        
        return FilmingLocationDto.builder()
                .id(location.getId())
                .name(location.getName())
                .address(location.getAddress())
                .description(location.getDescription())
                .sceneDescription(location.getSceneDescription())
                .latitude(location.getLatitude())
                .longitude(location.getLongitude())
                .dramaId(location.getDrama().getId())
                .dramaTitle(location.getDrama().getTitle())
                .averageRating(averageRating)
                .reviewCount(reviewCount)
                .images(location.getImages())
                .tags(location.getTags())
                .stampTypes(stampTypes)
                .build();
    }

    private LocationReviewDto convertToReviewDto(LocationReview review) {
        return LocationReviewDto.builder()
                .id(review.getId())
                .locationId(review.getFilmingLocation().getId())
                .userId(review.getUser().getId())
                .userUsername(review.getUser().getUsername())
                .title(review.getTitle())
                .content(review.getContent())
                .rating(review.getRating())
                .createdAt(review.getCreatedAt())
                .updatedAt(review.getUpdatedAt())
                .build();
    }
}
