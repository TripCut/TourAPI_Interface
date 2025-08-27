package com.tripcut.domain.stamp.service.impl;// package com.tripcut.domain.stamp.service.impl;

import com.tripcut.domain.filmingLocation.entity.FilmingLocation;
import com.tripcut.domain.filmingLocation.repository.FilmingLocationRepository;
import com.tripcut.domain.stamp.dto.StampDto;
import com.tripcut.domain.stamp.entity.Stamp;
import com.tripcut.domain.stamp.repository.StampRepository;
import com.tripcut.domain.stamp.service.StampService;
import com.tripcut.domain.user.entity.User;
import com.tripcut.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class StampServiceImpl implements StampService {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final StampRepository stampRepository;
    private final UserRepository userRepository;
    private final FilmingLocationRepository locationRepository;

    @Override
    public StampDto collect(Long authUserId, StampDto req) {
        User user = userRepository.findById(authUserId)
                .orElseThrow(() -> new NoSuchElementException("사용자를 찾을 수 없습니다. id=" + authUserId));

        Long locationId = req.getLocationId();
        FilmingLocation location = locationRepository.findById(locationId)
                .orElseThrow(() -> new NoSuchElementException("촬영지를 찾을 수 없습니다. id=" + locationId));

        // (선택) 중복 수집 방지 필요하면, 별도 exists 쿼리 추가/사용 권장

        Stamp s = new Stamp();
        s.setCollectedAt(req.getCollectedAt() == null || req.getCollectedAt().isBlank()
                ? LocalDateTime.now().format(FMT)
                : req.getCollectedAt());
        s.setStampImage(req.getStampImage());
        s.setStampType(req.getStampType());
        s.setStampDescription(req.getStampDescription());
        s.setStampPoints(req.getStampPoints());
        s.setFilmingLocation(location);
        s.setUser(user);

        return toDto(stampRepository.save(s));
    }

    @Transactional(readOnly = true)
    @Override
    public Page<StampDto> listByLocation(Long filmingLocationId, Pageable pageable) {
        return stampRepository.findByFilmingLocation_Id(filmingLocationId, pageable)
                .map(this::toDto);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<StampDto> listMineByLocation(Long authUserId, Long filmingLocationId, Pageable pageable) {
        return stampRepository.findByUser_IdAndFilmingLocation_Id(authUserId, filmingLocationId, pageable)
                .map(this::toDto);
    }

    @Transactional(readOnly = true)
    @Override
    public List<StampDto> listMineByDrama(Long authUserId, Long dramaId) {
        return stampRepository.findByUserIdAndDramaId(authUserId, dramaId)
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public Long countMineByDrama(Long authUserId, Long dramaId) {
        return stampRepository.getStampCountByUserIdAndDramaId(authUserId, dramaId);
    }

    @Transactional(readOnly = true)
    @Override
    public Integer totalPointsMine(Long authUserId) {
        Integer sum = stampRepository.getTotalPointsByUserId(authUserId);
        return sum == null ? 0 : sum;
    }

    @Transactional(readOnly = true)
    @Override
    public List<StampDto> recentMine(Long authUserId, int limit) {
        return stampRepository.findByUserIdOrderByCollectedAtDesc(authUserId)
                .stream().limit(limit).map(this::toDto).collect(Collectors.toList());
    }

    private StampDto toDto(Stamp s) {
        return StampDto.builder()
                .collectedAt(s.getCollectedAt())
                .stampImage(s.getStampImage())
                .stampType(s.getStampType())
                .stampDescription(s.getStampDescription())
                .stampPoints(s.getStampPoints())
                .locationId(s.getFilmingLocation().getId())
                .locationName(s.getFilmingLocation().getName())
                .locationLatitude(s.getFilmingLocation().getLat())
                .locationLongitude(s.getFilmingLocation().getLng())
                .userId(s.getUser().getId())
                .userUsername(s.getUser().getName())
                .build();
    }
}
