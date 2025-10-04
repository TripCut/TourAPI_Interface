package com.tripcut.domain.drama.service;

import com.tripcut.domain.drama.dto.DramaCreateRequest;
import com.tripcut.domain.drama.dto.DramaDto;
import com.tripcut.domain.drama.dto.DramaUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface DramaService {
    DramaDto create(DramaCreateRequest dramaCreateRequest, MultipartFile multipartFile);
    DramaDto update(Long id, DramaUpdateRequest dramaUpdateRequest);
    DramaDto read(Long id);
    Page<DramaDto> dramaPage(Pageable pageable);
    Page<DramaDto> searchByTitle(String title, Pageable pageable);
    void delete(Long id);
}
