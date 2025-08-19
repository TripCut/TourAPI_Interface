package com.tripcut.domain.drama.service.impl;

import com.tripcut.core.annotation.Transactional;
import com.tripcut.domain.drama.dto.CreateDramaDto;
import com.tripcut.domain.drama.entity.Drama;
import com.tripcut.domain.drama.repository.DramaRepository;
import com.tripcut.domain.drama.service.DramaService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DramaServiceImpl implements DramaService {
    private final DramaRepository dramaRepository;
    private final ModelMapper modelMapper;

    @Transactional
    @Override
    public CreateDramaDto createDrama(CreateDramaDto createDramaDto) {
        Drama drama = modelMapper.map(createDramaDto, Drama.class);
        dramaRepository.save(drama);
        return createDramaDto;
    }
}
