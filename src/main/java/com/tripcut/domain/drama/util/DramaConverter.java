package com.tripcut.domain.drama.util;

import com.tripcut.domain.drama.constant.DramaConstants;
import com.tripcut.domain.drama.dto.DramaDto;
import com.tripcut.domain.drama.dto.DramaReviewDto;
import com.tripcut.domain.drama.entity.Drama;
import com.tripcut.domain.drama.entity.DramaReview;
import com.tripcut.domain.drama.repository.DramaReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DramaConverter {

    private final DramaReviewRepository dramaReviewRepository;

    public DramaDto convertToDto(Drama drama) {
        Double averageRating = getAverageRating(drama.getId());
        Integer reviewCount = getReviewCount(drama.getId());

        List<String> filmingLocationNames = drama.getFilmingLocations().stream()
                .map(location -> location.getName())
                .collect(Collectors.toList());

        return DramaDto.builder()
                .id(drama.getId())
                .title(drama.getTitle())
                .description(drama.getDescription())
                .genre(drama.getGenre())
                .broadcastYear(drama.getBroadcastYear())
                .broadcastStation(drama.getBroadcastStation())
                .averageRating(averageRating)
                .reviewCount(reviewCount)
                .filmingLocationNames(filmingLocationNames)
                .build();
    }

    public DramaReviewDto convertToReviewDto(DramaReview review) {
        return DramaReviewDto.builder()
                .id(review.getId())
                .dramaId(review.getDrama().getId())
                .userId(review.getUser().getId())
                .userUsername(review.getUser().getUsername())
                .title(review.getTitle())
                .content(review.getContent())
                .rating(review.getRating())
                .createdAt(review.getCreatedAt())
                .updatedAt(review.getUpdatedAt())
                .build();
    }

    public List<DramaDto> convertToDtoList(List<Drama> dramas) {
        return dramas.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<DramaReviewDto> convertToReviewDtoList(List<DramaReview> reviews) {
        return reviews.stream()
                .map(this::convertToReviewDto)
                .collect(Collectors.toList());
    }

    private Double getAverageRating(Long dramaId) {
        return dramaReviewRepository.getAverageRatingByDramaId(dramaId);
    }

    private Integer getReviewCount(Long dramaId) {
        return dramaReviewRepository.getReviewCountByDramaId(dramaId);
    }
} 