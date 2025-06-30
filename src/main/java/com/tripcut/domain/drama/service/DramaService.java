package com.tripcut.domain.drama.service;

import com.tripcut.domain.drama.dto.DramaDto;
import com.tripcut.domain.drama.dto.DramaReviewDto;
import com.tripcut.domain.drama.entity.Drama;
import com.tripcut.domain.drama.entity.DramaReview;

import java.util.List;

public interface DramaService {
    
    // 드라마 CRUD
    DramaDto createDrama(DramaDto dramaDto);
    DramaDto getDramaById(Long id);
    List<DramaDto> getAllDramas();
    DramaDto updateDrama(Long id, DramaDto dramaDto);
    void deleteDrama(Long id);
    
    // 드라마 검색
    List<DramaDto> searchDramasByKeyword(String keyword);
    List<DramaDto> getDramasByGenre(String genre);
    List<DramaDto> getDramasByYear(String year);
    List<DramaDto> getDramasByStation(String station);
    List<DramaDto> getDramasByFilmingLocation(String locationName);
    List<DramaDto> getTopRatedDramas(int limit);
    
    // 드라마별 촬영지 조회
    List<String> getFilmingLocationsByDramaId(Long dramaId);
    
    // 리뷰 관리
    DramaReviewDto createReview(DramaReviewDto reviewDto);
    List<DramaReviewDto> getReviewsByDramaId(Long dramaId);
    DramaReviewDto updateReview(Long reviewId, DramaReviewDto reviewDto);
    void deleteReview(Long reviewId);
    
    // 통계
    Double getAverageRating(Long dramaId);
    Integer getReviewCount(Long dramaId);
}
