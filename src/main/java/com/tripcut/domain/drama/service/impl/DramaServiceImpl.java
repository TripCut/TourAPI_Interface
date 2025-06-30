package com.tripcut.domain.drama.service.impl;

import com.tripcut.core.annotation.Logging;
import com.tripcut.core.annotation.Metrics;
import com.tripcut.core.annotation.TourAPI;
import com.tripcut.domain.drama.constant.DramaConstants;
import com.tripcut.domain.drama.dto.DramaDto;
import com.tripcut.domain.drama.dto.DramaReviewDto;
import com.tripcut.domain.drama.entity.Drama;
import com.tripcut.domain.drama.entity.DramaReview;
import com.tripcut.domain.drama.repository.DramaRepository;
import com.tripcut.domain.drama.repository.DramaReviewRepository;
import com.tripcut.domain.drama.service.DramaService;
import com.tripcut.domain.drama.util.DramaConverter;
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
public class DramaServiceImpl implements DramaService {

    private final DramaRepository dramaRepository;
    private final DramaReviewRepository dramaReviewRepository;
    private final UserRepository userRepository;
    private final DramaConverter dramaConverter;    // 상수 기반으로 관심사 분리

    @Override
    @Logging
    @Metrics(name = DramaConstants.METRIC_DRAMA_CREATE)
    @TourAPI(service = DramaConstants.TOUR_API_SERVICE_DRAMA, operation = DramaConstants.TOUR_API_OPERATION_CREATE)
    public DramaDto createDrama(DramaDto dramaDto) {
        Drama drama = Drama.builder()
                .title(dramaDto.getTitle())
                .description(dramaDto.getDescription())
                .genre(dramaDto.getGenre())
                .broadcastYear(dramaDto.getBroadcastYear())
                .broadcastStation(dramaDto.getBroadcastStation())
                .build();
        
        Drama savedDrama = dramaRepository.save(drama);
        return dramaConverter.convertToDto(savedDrama);
    }

    @Override
    @Logging
    @Metrics(name = DramaConstants.METRIC_DRAMA_GET_BY_ID)
    @TourAPI(service = DramaConstants.TOUR_API_SERVICE_DRAMA, operation = DramaConstants.TOUR_API_OPERATION_DETAIL)
    public DramaDto getDramaById(Long id) {
        Drama drama = dramaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(DramaConstants.ERROR_DRAMA_NOT_FOUND + id));
        return dramaConverter.convertToDto(drama);
    }

    @Override
    @Logging
    @Metrics(name = DramaConstants.METRIC_DRAMA_GET_ALL)
    @TourAPI(service = DramaConstants.TOUR_API_SERVICE_DRAMA, operation = DramaConstants.TOUR_API_OPERATION_LIST)
    public List<DramaDto> getAllDramas() {
        return dramaConverter.convertToDtoList(dramaRepository.findAll());
    }

    @Override
    @Logging
    @Metrics(name = DramaConstants.METRIC_DRAMA_UPDATE)
    @TourAPI(service = DramaConstants.TOUR_API_SERVICE_DRAMA, operation = DramaConstants.TOUR_API_OPERATION_UPDATE)
    public DramaDto updateDrama(Long id, DramaDto dramaDto) {
        Drama drama = dramaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(DramaConstants.ERROR_DRAMA_NOT_FOUND + id));
        
        drama.setTitle(dramaDto.getTitle());
        drama.setDescription(dramaDto.getDescription());
        drama.setGenre(dramaDto.getGenre());
        drama.setBroadcastYear(dramaDto.getBroadcastYear());
        drama.setBroadcastStation(dramaDto.getBroadcastStation());
        
        Drama updatedDrama = dramaRepository.save(drama);
        return dramaConverter.convertToDto(updatedDrama);
    }

    @Override
    @Logging
    @Metrics(name = DramaConstants.METRIC_DRAMA_DELETE)
    @TourAPI(service = DramaConstants.TOUR_API_SERVICE_DRAMA, operation = DramaConstants.TOUR_API_OPERATION_DELETE)
    public void deleteDrama(Long id) {
        if (!dramaRepository.existsById(id)) {
            throw new RuntimeException(DramaConstants.ERROR_DRAMA_NOT_FOUND + id);
        }
        dramaRepository.deleteById(id);
    }

    @Override
    @Logging
    @Metrics(name = DramaConstants.METRIC_DRAMA_SEARCH)
    @TourAPI(service = DramaConstants.TOUR_API_SERVICE_DRAMA, operation = DramaConstants.TOUR_API_OPERATION_SEARCH)
    public List<DramaDto> searchDramasByKeyword(String keyword) {
        return dramaConverter.convertToDtoList(dramaRepository.findByKeyword(keyword));
    }

    @Override
    @Logging
    @Metrics(name = DramaConstants.METRIC_DRAMA_GET_BY_GENRE)
    public List<DramaDto> getDramasByGenre(String genre) {
        return dramaConverter.convertToDtoList(dramaRepository.findByGenre(genre));
    }

    @Override
    @Logging
    @Metrics(name = DramaConstants.METRIC_DRAMA_GET_BY_YEAR)
    public List<DramaDto> getDramasByYear(String year) {
        return dramaConverter.convertToDtoList(dramaRepository.findByBroadcastYear(year));
    }

    @Override
    @Logging
    @Metrics(name = DramaConstants.METRIC_DRAMA_GET_BY_STATION)
    public List<DramaDto> getDramasByStation(String station) {
        return dramaConverter.convertToDtoList(dramaRepository.findByBroadcastStation(station));
    }

    @Override
    @Logging
    @Metrics(name = DramaConstants.METRIC_DRAMA_GET_BY_LOCATION)
    public List<DramaDto> getDramasByFilmingLocation(String locationName) {
        return dramaConverter.convertToDtoList(dramaRepository.findByFilmingLocationName(locationName));
    }

    @Override
    @Logging
    @Metrics(name = DramaConstants.METRIC_DRAMA_GET_TOP_RATED)
    public List<DramaDto> getTopRatedDramas(int limit) {
        int actualLimit = limit > 0 ? limit : DramaConstants.DEFAULT_TOP_RATED_LIMIT;
        return dramaRepository.findAllOrderByAverageRating().stream()
                .limit(actualLimit)
                .map(dramaConverter::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Logging
    @Metrics(name = DramaConstants.METRIC_DRAMA_GET_LOCATIONS)
    public List<String> getFilmingLocationsByDramaId(Long dramaId) {
        Drama drama = dramaRepository.findById(dramaId)
                .orElseThrow(() -> new RuntimeException(DramaConstants.ERROR_DRAMA_NOT_FOUND + dramaId));
        
        return drama.getFilmingLocations().stream()
                .map(location -> location.getName())
                .collect(Collectors.toList());
    }

    @Override
    @Logging
    @Metrics(name = DramaConstants.METRIC_DRAMA_CREATE_REVIEW)
    public DramaReviewDto createReview(DramaReviewDto reviewDto) {
        Drama drama = dramaRepository.findById(reviewDto.getDramaId())
                .orElseThrow(() -> new RuntimeException(DramaConstants.ERROR_DRAMA_NOT_FOUND + reviewDto.getDramaId()));
        
        User user = userRepository.findById(reviewDto.getUserId())
                .orElseThrow(() -> new RuntimeException(DramaConstants.ERROR_USER_NOT_FOUND + reviewDto.getUserId()));
        
        // 평점 유효성 검사
        if (reviewDto.getRating() < DramaConstants.DEFAULT_REVIEW_RATING_MIN || 
            reviewDto.getRating() > DramaConstants.DEFAULT_REVIEW_RATING_MAX) {
            throw new RuntimeException("Rating must be between " + DramaConstants.DEFAULT_REVIEW_RATING_MIN + 
                                     " and " + DramaConstants.DEFAULT_REVIEW_RATING_MAX);
        }
        
        DramaReview review = new DramaReview();
        review.setDrama(drama);
        review.setUser(user);
        review.setTitle(reviewDto.getTitle());
        review.setContent(reviewDto.getContent());
        review.setRating(reviewDto.getRating());
        
        DramaReview savedReview = dramaReviewRepository.save(review);
        return dramaConverter.convertToReviewDto(savedReview);
    }

    @Override
    @Logging
    @Metrics(name = DramaConstants.METRIC_DRAMA_GET_REVIEWS)
    public List<DramaReviewDto> getReviewsByDramaId(Long dramaId) {
        return dramaConverter.convertToReviewDtoList(dramaReviewRepository.findByDramaId(dramaId));
    }

    @Override
    @Logging
    @Metrics(name = DramaConstants.METRIC_DRAMA_UPDATE_REVIEW)
    public DramaReviewDto updateReview(Long reviewId, DramaReviewDto reviewDto) {
        DramaReview review = dramaReviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException(DramaConstants.ERROR_REVIEW_NOT_FOUND + reviewId));
        
        // 평점 유효성 검사
        if (reviewDto.getRating() < DramaConstants.DEFAULT_REVIEW_RATING_MIN || 
            reviewDto.getRating() > DramaConstants.DEFAULT_REVIEW_RATING_MAX) {
            throw new RuntimeException("Rating must be between " + DramaConstants.DEFAULT_REVIEW_RATING_MIN + 
                                     " and " + DramaConstants.DEFAULT_REVIEW_RATING_MAX);
        }
        
        review.setTitle(reviewDto.getTitle());
        review.setContent(reviewDto.getContent());
        review.setRating(reviewDto.getRating());
        
        DramaReview updatedReview = dramaReviewRepository.save(review);
        return dramaConverter.convertToReviewDto(updatedReview);
    }

    @Override
    @Logging
    @Metrics(name = DramaConstants.METRIC_DRAMA_DELETE_REVIEW)
    public void deleteReview(Long reviewId) {
        if (!dramaReviewRepository.existsById(reviewId)) {
            throw new RuntimeException(DramaConstants.ERROR_REVIEW_NOT_FOUND + reviewId);
        }
        dramaReviewRepository.deleteById(reviewId);
    }

    @Override
    @Logging
    @Metrics(name = DramaConstants.METRIC_DRAMA_GET_AVERAGE_RATING)
    public Double getAverageRating(Long dramaId) {
        return dramaReviewRepository.getAverageRatingByDramaId(dramaId);
    }

    @Override
    @Logging
    @Metrics(name = DramaConstants.METRIC_DRAMA_GET_REVIEW_COUNT)
    public Integer getReviewCount(Long dramaId) {
        return dramaReviewRepository.getReviewCountByDramaId(dramaId);
    }
}
