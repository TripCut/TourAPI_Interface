package com.tripcut.domain.filmingLocation.service.impl;

import com.tripcut.domain.drama.entity.Drama;
import com.tripcut.domain.drama.repository.DramaRepository;
import com.tripcut.domain.filmingLocation.dto.FilmingLocationCreateRequest;
import com.tripcut.domain.filmingLocation.dto.FilmingLocationDto;
import com.tripcut.domain.filmingLocation.dto.FilmingLocationUpdateRequest;
import com.tripcut.domain.filmingLocation.entity.FilmingLocation;
import com.tripcut.domain.filmingLocation.repository.FilmingLocationRepository;
import com.tripcut.domain.filmingLocation.service.FilmingLocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Transactional
public class FilmingLocationServiceImpl implements FilmingLocationService {

    private final FilmingLocationRepository locationRepository;
    private final DramaRepository dramaRepository;

    @Override
    public FilmingLocationDto create(FilmingLocationCreateRequest req) {
        Drama drama = dramaRepository.findById(req.getDramaId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "드라마가 없습니다. id=" + req.getDramaId()));

        FilmingLocation f = FilmingLocation.builder()
                .name(req.getName())
                .address(req.getAddress())
                .description(req.getDescription())
                .sceneDescription(req.getSceneDescription())
                .lat(req.getLat())
                .lng(req.getLng())
                .drama(drama)
                .build();

        if (req.getImages() != null) f.getImages().addAll(req.getImages());
        if (req.getTags() != null) f.getTags().addAll(req.getTags());

        return toDto(locationRepository.save(f));
    }

    @Transactional(readOnly = true)
    @Override
    public FilmingLocationDto get(Long id) {
        FilmingLocation f = locationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "촬영지를 찾을 수 없습니다. id=" + id));
        return toDto(f);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<FilmingLocationDto> list(Long dramaId, String keyword, String tag, Pageable pageable) {
        if (dramaId != null) {
            return locationRepository.findByDrama_Id(dramaId, pageable).map(this::toDto);
        }
        if (tag != null && !tag.isBlank()) {
            return locationRepository.findByTag(tag, pageable).map(this::toDto);
        }
        if (keyword != null && !keyword.isBlank()) {
            return locationRepository.findByNameContainingIgnoreCase(keyword, pageable).map(this::toDto);
        }
        return locationRepository.findAll(pageable).map(this::toDto);
    }

    @Override
    public FilmingLocationDto update(Long id, FilmingLocationUpdateRequest req) {
        FilmingLocation f = locationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "촬영지를 찾을 수 없습니다. id=" + id));

        if (!f.getDrama().getId().equals(req.getDramaId())) {
            // 드라마 변경 허용 시 새 드라마 세팅
            Drama drama = dramaRepository.findById(req.getDramaId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "드라마가 없습니다. id=" + req.getDramaId()));
            f.setDrama(drama);
        }

        f.setName(req.getName());
        f.setAddress(req.getAddress());
        f.setDescription(req.getDescription());
        f.setSceneDescription(req.getSceneDescription());
        f.setLat(req.getLat());
        f.setLng(req.getLng());

        // 리스트 교체(전체 덮어쓰기)
        f.getImages().clear();
        if (req.getImages() != null) f.getImages().addAll(req.getImages());
        f.getTags().clear();
        if (req.getTags() != null) f.getTags().addAll(req.getTags());

        return toDto(f); // JPA dirty checking
    }

    @Override
    public void delete(Long id) {
        FilmingLocation f = locationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "촬영지를 찾을 수 없습니다. id=" + id));
        locationRepository.delete(f);
    }

    private FilmingLocationDto toDto(FilmingLocation f) {
        return FilmingLocationDto.builder()
                .name(f.getName())
                .address(f.getAddress())
                .description(f.getDescription())
                .sceneDescription(f.getSceneDescription())
                .lat(f.getLat())
                .lng(f.getLng())
                .dramaId(f.getDrama() != null ? f.getDrama().getId() : null)
                .dramaTitle(f.getDrama() != null ? f.getDrama().getTitle() : null)
                .images(f.getImages())
                .tags(f.getTags())
                .build();
    }
}
