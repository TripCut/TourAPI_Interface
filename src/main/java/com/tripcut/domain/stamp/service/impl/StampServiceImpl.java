package com.tripcut.domain.stamp.service.impl;

import com.tripcut.core.annotation.Logging;
import com.tripcut.core.annotation.Metrics;
import com.tripcut.core.annotation.StampRally;
import com.tripcut.domain.location.entity.FilmingLocation;
import com.tripcut.domain.location.repository.FilmingLocationRepository;
import com.tripcut.domain.stamp.dto.StampDto;
import com.tripcut.domain.stamp.entity.Stamp;
import com.tripcut.domain.stamp.repository.StampRepository;
import com.tripcut.domain.stamp.service.StampService;
import com.tripcut.domain.user.entity.User;
import com.tripcut.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class StampServiceImpl implements StampService {

    private final StampRepository stampRepository;
    private final UserRepository userRepository;
    private final FilmingLocationRepository locationRepository;

    @Override
    @Logging
    @Metrics(name = "")
    @StampRally(dramaId = "dynamic", requiredLocations = {"dynamic"}, validateLocation = true, maxDistance = 100)
    public StampDto collectStamp(StampDto stampDto) {
        User user = userRepository.findById(stampDto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + stampDto.getUserId()));
        
        FilmingLocation location = locationRepository.findById(stampDto.getLocationId())
                .orElseThrow(() -> new RuntimeException("Location not found with id: " + stampDto.getLocationId()));
        
        // 이미 수집한 스탬프인지 확인
        List<Stamp> existingStamps = stampRepository.findByUser_IdAndFilmingLocation_Id(stampDto.getUserId(), stampDto.getLocationId());
        if (!existingStamps.isEmpty()) {
            throw new RuntimeException("Stamp already collected for this location");
        }
        
        Stamp stamp = new Stamp();
        stamp.setCollectedAt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        stamp.setStampImage(stampDto.getStampImage());
        stamp.setStampType(stampDto.getStampType());
        stamp.setStampDescription(stampDto.getStampDescription());
        stamp.setStampPoints(stampDto.getStampPoints());
        stamp.setFilmingLocation(location);
        stamp.setUser(user);
        
        Stamp savedStamp = stampRepository.save(stamp);
        return convertToDto(savedStamp);
    }

    @Override
    @Logging
    @Metrics(name = "")
    public StampDto getStampById(Long id) {
        Stamp stamp = stampRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Stamp not found with id: " + id));
        return convertToDto(stamp);
    }

    @Override
    @Logging
    @Metrics(name = "")
    public List<StampDto> getStampsByUserId(Long userId) {
        return stampRepository.findByUserId(userId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Logging
    @Metrics(name = "")
    public List<StampDto> getStampsByLocationId(Long locationId) {
        return stampRepository.findByLocationId(locationId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Logging
    @Metrics(name = "")
    public List<StampDto> getStampsByDramaId(Long dramaId) {
        // 모든 사용자의 해당 드라마 스탬프 조회
        return stampRepository.findAll().stream()
                .filter(stamp -> stamp.getFilmingLocation().getDrama().getId().equals(dramaId))
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Logging
    @Metrics(name = "")
    public Boolean validateLocation(Long locationId, Double userLatitude, Double userLongitude, Double maxDistance) {
        FilmingLocation location = locationRepository.findById(locationId)
                .orElseThrow(() -> new RuntimeException("Location not found with id: " + locationId));
        
        Double distance = calculateDistance(
                userLatitude, userLongitude,
                location.getLatitude(), location.getLongitude()
        );
        
        return distance <= maxDistance;
    }

    @Override
    @Logging
    @Metrics(name = "")
    @StampRally(dramaId = "dynamic", requiredLocations = {"dynamic"}, validateLocation = true, maxDistance = 100)
    public StampDto collectStampWithLocationValidation(StampDto stampDto, Double userLatitude, Double userLongitude) {
        // 위치 검증
        Boolean isValid = validateLocation(stampDto.getLocationId(), userLatitude, userLongitude, 100.0);
        if (!isValid) {
            throw new RuntimeException("Location validation failed. You are too far from the filming location.");
        }
        
        // 위치 검증 성공 시 스탬프 수집
        stampDto.setIsLocationValidated(true);
        stampDto.setUserLatitude(userLatitude);
        stampDto.setUserLongitude(userLongitude);
        
        return collectStamp(stampDto);
    }

    @Override
    @Logging
    @Metrics(name = "")
    public Map<String, Object> getRallyProgress(Long userId, Long dramaId) {
        Long collectedStamps = getStampCountByUserIdAndDramaId(userId, dramaId);
        Integer totalStamps = locationRepository.findByDramaId(dramaId).size();
        Long progressPercentage = totalStamps > 0 ? (collectedStamps * 100) / totalStamps : 0;
        
        Map<String, Object> progress = new HashMap<>();
        progress.put("userId", userId);
        progress.put("dramaId", dramaId);
        progress.put("collectedStamps", collectedStamps);
        progress.put("totalStamps", totalStamps);
        progress.put("progressPercentage", progressPercentage);
        progress.put("isCompleted", collectedStamps >= totalStamps);
        
        return progress;
    }

    @Override
    @Logging
    @Metrics(name = "")
    public List<Map<String, Object>> getRallyProgressByDrama(Long userId) {
        // 사용자가 참여한 모든 드라마의 랠리 진행 상황 조회
        List<Long> dramaIds = stampRepository.findByUserId(userId).stream()
                .map(stamp -> stamp.getFilmingLocation().getDrama().getId())
                .distinct()
                .collect(Collectors.toList());
        
        return dramaIds.stream()
                .map(dramaId -> getRallyProgress(userId, dramaId))
                .collect(Collectors.toList());
    }

    @Override
    @Logging
    @Metrics(name = "")
    public Integer getTotalPointsByUserId(Long userId) {
        return stampRepository.getTotalPointsByUserId(userId);
    }

    @Override
    @Logging
    @Metrics(name = "")
    public Long getStampCountByUserIdAndDramaId(Long userId, Long dramaId) {
        return stampRepository.getStampCountByUserIdAndDramaId(userId, dramaId);
    }

    @Override
    @Logging
    @Metrics(name = "")
    public List<StampDto> getRecentStampsByUserId(Long userId, int limit) {
        return stampRepository.findByUserIdOrderByCollectedAtDesc(userId).stream()
                .limit(limit)
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Logging
    @Metrics(name = "")
    public Map<String, Integer> getStampStatisticsByUserId(Long userId) {
        List<Stamp> userStamps = stampRepository.findByUserId(userId);
        
        Map<String, Integer> statistics = new HashMap<>();
        statistics.put("totalStamps", userStamps.size());
        statistics.put("totalPoints", getTotalPointsByUserId(userId));
        
        // 드라마별 스탬프 수
        Map<Long, Long> dramaStampCounts = userStamps.stream()
                .collect(Collectors.groupingBy(
                        stamp -> stamp.getFilmingLocation().getDrama().getId(),
                        Collectors.counting()
                ));
        
        statistics.put("participatedDramas", dramaStampCounts.size());
        
        return statistics;
    }

    @Override
    @Logging
    @Metrics(name = "")
    public void deleteStamp(Long stampId) {
        if (!stampRepository.existsById(stampId)) {
            throw new RuntimeException("Stamp not found with id: " + stampId);
        }
        stampRepository.deleteById(stampId);
    }

    private Double calculateDistance(Double lat1, Double lon1, Double lat2, Double lon2) {
        final int R = 6371; // 지구의 반지름 (km)
        
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        
        return R * c * 1000; // 미터 단위로 반환
    }

    private StampDto convertToDto(Stamp stamp) {
        return StampDto.builder()
                .id(stamp.getId())
                .collectedAt(stamp.getCollectedAt())
                .stampImage(stamp.getStampImage())
                .stampType(stamp.getStampType())
                .stampDescription(stamp.getStampDescription())
                .stampPoints(stamp.getStampPoints())
                .locationId(stamp.getFilmingLocation().getId())
                .locationName(stamp.getFilmingLocation().getName())
                .userId(stamp.getUser().getId())
                .userUsername(stamp.getUser().getName())
                .locationLatitude(stamp.getFilmingLocation().getLatitude())
                .locationLongitude(stamp.getFilmingLocation().getLongitude())
                .build();
    }
}
