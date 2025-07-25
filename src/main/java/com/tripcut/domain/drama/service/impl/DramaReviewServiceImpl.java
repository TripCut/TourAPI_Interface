package com.tripcut.domain.drama.service.impl;

import com.tripcut.domain.drama.dto.DramaReviewRequestDto;
import com.tripcut.domain.drama.dto.DramaReviewResponseDto;
import com.tripcut.domain.drama.entity.Drama;
import com.tripcut.domain.drama.entity.DramaReview;
import com.tripcut.domain.drama.repository.DramaRepository;
import com.tripcut.domain.drama.repository.DramaReviewRepository;
import com.tripcut.domain.drama.service.DramaReviewService;
import com.tripcut.domain.user.entity.User;
import com.tripcut.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DramaReviewServiceImpl implements DramaReviewService {
    private final DramaReviewRepository reviewRepository;
    private final DramaRepository dramaRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public DramaReviewResponseDto createReview(DramaReviewRequestDto dto, Long userId) {
        Drama drama = dramaRepository.findById(dto.getDramaId()).orElseThrow();
        User user = userRepository.findById(userId).orElseThrow();
        DramaReview review = new DramaReview();
        review.setDrama(drama);
        review.setUser(user);
        review.setRating(dto.getRating());
        review.setContent(dto.getContent());
        review.setCreatedAt(LocalDateTime.now().toString());
        review.setUpdatedAt(LocalDateTime.now().toString());
        DramaReview saved = reviewRepository.save(review);
        return modelMapper.map(saved, DramaReviewResponseDto.class);
    }

    @Override
    public DramaReviewResponseDto updateReview(Long reviewId, DramaReviewRequestDto dto, Long userId) {
        DramaReview review = reviewRepository.findById(reviewId).orElseThrow();
        if (!review.getUser().getId().equals(userId)) throw new RuntimeException("권한 없음");
        review.setRating(dto.getRating());
        review.setContent(dto.getContent());
        review.setUpdatedAt(LocalDateTime.now().toString());
        DramaReview saved = reviewRepository.save(review);
        return modelMapper.map(saved, DramaReviewResponseDto.class);
    }

    @Override
    public void deleteReview(Long reviewId, Long userId) {
        DramaReview review = reviewRepository.findById(reviewId).orElseThrow();
        if (!review.getUser().getId().equals(userId)) throw new RuntimeException("권한 없음");
        reviewRepository.deleteById(reviewId);
    }

    @Override
    public DramaReviewResponseDto getReview(Long reviewId) {
        DramaReview review = reviewRepository.findById(reviewId).orElseThrow();
        return modelMapper.map(review, DramaReviewResponseDto.class);
    }

    @Override
    public List<DramaReviewResponseDto> getReviewsByDrama(Long dramaId) {
        return reviewRepository.findAll().stream()
                .filter(r -> r.getDrama().getId().equals(dramaId))
                .map(r -> modelMapper.map(r, DramaReviewResponseDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<DramaReviewResponseDto> getReviewsByUser(Long userId) {
        return reviewRepository.findAll().stream()
                .filter(r -> r.getUser().getId().equals(userId))
                .map(r -> modelMapper.map(r, DramaReviewResponseDto.class))
                .collect(Collectors.toList());
    }
} 