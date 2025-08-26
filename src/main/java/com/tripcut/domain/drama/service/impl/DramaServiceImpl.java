package com.tripcut.domain.drama.service.impl;

import com.tripcut.core.annotation.Transactional;
import com.tripcut.domain.drama.dto.DramaCreateRequest;
import com.tripcut.domain.drama.dto.DramaDto;
import com.tripcut.domain.drama.dto.DramaUpdateRequest;
import com.tripcut.domain.drama.entity.Drama;
import com.tripcut.domain.drama.repository.DramaRepository;
import com.tripcut.domain.drama.service.DramaService;
import com.tripcut.domain.drama.util.DramaConverter;
import com.tripcut.domain.location.entity.FilmingLocation;
import com.tripcut.domain.location.repository.FilmingLocationRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DramaServiceImpl implements DramaService {
    private final DramaRepository dramaRepository;
    private final FilmingLocationRepository filmingLocationRepository;
    private final DramaConverter dramaConverter;

    @Transactional
    @Override
    public DramaDto create(DramaCreateRequest req) {
        if (dramaRepository.existsByTitle(req.getTitle())) {
            throw new IllegalArgumentException("이미 존재하는 제목입니다: " + req.getTitle());
        }

        Drama drama = new Drama();
        applyBasics(drama, req.getTitle(), req.getDescription(), req.getGenre(),
                req.getBroadcastYear(), req.getBroadcastStation());

        // 자식(촬영지) 생성/연결
        replaceCreateLocations(drama, req.getFilmingLocations());

        // (옵션) tags는 엔티티에 없으므로 저장 로직이 없어요. 엔티티 추가 시 반영.
        Drama saved = dramaRepository.save(drama);
        return dramaConverter.convertToDto(saved);
    }


    @Transactional
    @Override
    public DramaDto read(Long id) {
        Drama drama = dramaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Drama not found: " + id));
        return dramaConverter.convertToDto(drama);
    }

    @Transactional
    @Override
    public Page<DramaDto> dramaPage(Pageable pageable) {
        return dramaRepository.findAll(pageable)
                .map(dramaConverter::convertToDto);
    }

    @Transactional
    @Override
    public DramaDto update(Long id, DramaUpdateRequest req) {
        Drama drama = dramaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Drama not found: " + id));

        if (req.getTitle() != null) drama.setTitle(req.getTitle());
        if (req.getDescription() != null) drama.setDescription(req.getDescription());
        if (req.getGenre() != null) drama.setGenre(req.getGenre());
        if (req.getBroadcastYear() != null) drama.setBroadcastYear(req.getBroadcastYear());
        if (req.getBroadcastStation() != null) drama.setBroadcastStation(req.getBroadcastStation());

        // 촬영지 전체 교체(null이면 변경 없음)
        if (req.getFilmingLocations() != null) {
            replaceUpdateLocations(drama, req.getFilmingLocations());
        }

        return dramaConverter.convertToDto(drama);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        if (!dramaRepository.existsById(id)) {
            throw new EntityNotFoundException("Drama not found: " + id);
        }
        dramaRepository.deleteById(id);
    }

    // 편의 메서드
    private void applyBasics(Drama d, String title, String desc, String genre, String year, String station) {
        d.setTitle(title);
        d.setDescription(desc);
        d.setGenre(genre);
        d.setBroadcastYear(year);
        d.setBroadcastStation(station);
    }

    private void replaceUpdateLocations(Drama drama, List<DramaUpdateRequest.FilmingLocationPayload> payloads) {
        replaceLocationsInternal(drama, payloads);
    }

    private void replaceCreateLocations(Drama drama, List<DramaCreateRequest.FilmingLocationPayload> payloads) {
        replaceLocationsInternal(drama, payloads);
    }

    private <T> void replaceLocationsInternal(Drama drama, List<T> payloads) {
        drama.clearFilmingLocations(); // orphanRemoval=true 권장

        if (payloads == null || payloads.isEmpty()) return;

        for (T p : payloads) {
            // 각각의 getter를 메서드 레퍼런스로 뽑아냅니다.
            String name     = (p instanceof DramaCreateRequest.FilmingLocationPayload c) ? c.getName()
                    : ((DramaUpdateRequest.FilmingLocationPayload) p).getName();
            String address  = (p instanceof DramaCreateRequest.FilmingLocationPayload c) ? c.getAddress()
                    : ((DramaUpdateRequest.FilmingLocationPayload) p).getAddress();
            Double lat      = (p instanceof DramaCreateRequest.FilmingLocationPayload c) ? c.getLat()
                    : ((DramaUpdateRequest.FilmingLocationPayload) p).getLat();
            Double lng      = (p instanceof DramaCreateRequest.FilmingLocationPayload c) ? c.getLng()
                    : ((DramaUpdateRequest.FilmingLocationPayload) p).getLng();

            FilmingLocation loc = FilmingLocation.builder()
                    .name(name)
                    .address(address)
                    .lat(lat)
                    .lng(lng)
                    .build();
            drama.addFilmingLocation(loc);
        }
    }
}

