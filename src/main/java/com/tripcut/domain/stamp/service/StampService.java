package com.tripcut.domain.stamp.service;

import com.tripcut.domain.stamp.dto.StampDto;

import java.util.List;
import java.util.Map;

public interface StampService {
    
    // 스탬프 수집
    StampDto collectStamp(StampDto stampDto);
    StampDto getStampById(Long id);
    List<StampDto> getStampsByUserId(Long userId);
    List<StampDto> getStampsByLocationId(Long locationId);
    List<StampDto> getStampsByDramaId(Long dramaId);
    
    // 위치 검증
    Boolean validateLocation(Long locationId, Double userLatitude, Double userLongitude, Double maxDistance);
    StampDto collectStampWithLocationValidation(StampDto stampDto, Double userLatitude, Double userLongitude);
    
    // 랠리 진행 상황 관리
    Map<String, Object> getRallyProgress(Long userId, Long dramaId);
    List<Map<String, Object>> getRallyProgressByDrama(Long userId);
    Integer getTotalPointsByUserId(Long userId);
    Integer getStampCountByUserIdAndDramaId(Long userId, Long dramaId);
    
    // 스탬프 통계
    List<StampDto> getRecentStampsByUserId(Long userId, int limit);
    Map<String, Integer> getStampStatisticsByUserId(Long userId);
    
    // 스탬프 삭제
    void deleteStamp(Long stampId);
}
