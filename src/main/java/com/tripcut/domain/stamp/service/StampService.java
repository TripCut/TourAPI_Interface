package com.tripcut.domain.stamp.service;

import com.tripcut.domain.stamp.dto.StampDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StampService {
    StampDto collect(Long authUserId, StampDto req);

    // 촬영지 전체 스탬프(모든 사용자)
    Page<StampDto> listByLocation(Long filmingLocationId, Pageable pageable);

    // 내 스탬프: 특정 촬영지
    Page<StampDto> listMineByLocation(Long authUserId, Long filmingLocationId, Pageable pageable);

    // 내 스탬프: 특정 드라마
    List<StampDto> listMineByDrama(Long authUserId, Long dramaId);

    // 내 진행도/집계
    Long countMineByDrama(Long authUserId, Long dramaId);
    Integer totalPointsMine(Long authUserId);
    List<StampDto> recentMine(Long authUserId, int limit);
}