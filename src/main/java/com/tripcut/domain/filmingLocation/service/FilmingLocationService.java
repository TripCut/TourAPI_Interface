package com.tripcut.domain.filmingLocation.service;

import com.tripcut.domain.filmingLocation.dto.FilmingLocationCreateRequest;
import com.tripcut.domain.filmingLocation.dto.FilmingLocationDto;
import com.tripcut.domain.filmingLocation.dto.FilmingLocationUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FilmingLocationService {
    FilmingLocationDto create(FilmingLocationCreateRequest req);
    FilmingLocationDto get(Long id);
    Page<FilmingLocationDto> list(Long dramaId, String keyword, String tag, Pageable pageable);
    FilmingLocationDto update(Long id, FilmingLocationUpdateRequest req);
    void delete(Long id);
}
