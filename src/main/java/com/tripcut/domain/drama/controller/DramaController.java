package com.tripcut.domain.drama.controller;

import com.tripcut.core.controller.BaseController;
import com.tripcut.domain.drama.dto.DramaRequestDto;
import com.tripcut.domain.drama.dto.DramaResponseDto;
import com.tripcut.domain.drama.entity.Drama;
import com.tripcut.domain.drama.service.DramaService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/dramas")
@RequiredArgsConstructor
public class DramaController extends BaseController {
    private final DramaService dramaService;
    private final ModelMapper modelMapper;

    @GetMapping
    public Object getAllDramas() {
        List<DramaResponseDto> result = dramaService.getAllDramas().stream()
                .map(drama -> modelMapper.map(drama, DramaResponseDto.class))
                .collect(Collectors.toList());
        return success(result);
    }

    @GetMapping("/{id}")
    public Object getDramaById(@PathVariable Long id) {
        Drama drama = dramaService.getDramaById(id);
        if (drama == null) return notFound();
        return success(modelMapper.map(drama, DramaResponseDto.class));
    }

    @PostMapping
    public Object createDrama(@RequestBody DramaRequestDto requestDto) {
        Drama drama = modelMapper.map(requestDto, Drama.class);
        Drama saved = dramaService.createDrama(drama);
        return success(modelMapper.map(saved, DramaResponseDto.class));
    }

    @DeleteMapping("/{id}")
    public Object deleteDrama(@PathVariable Long id) {
        dramaService.deleteDrama(id);
        return success(null);
    }

    // 검색 API
    @GetMapping("/search")
    public Object searchDramas(@RequestParam(required = false) String keyword,
                               @RequestParam(required = false) String genre,
                               @RequestParam(required = false) String station) {
        List<DramaResponseDto> result = dramaService.searchDramas(keyword, genre, station).stream()
                .map(drama -> modelMapper.map(drama, DramaResponseDto.class))
                .collect(Collectors.toList());
        return success(result);
    }

    // 평균 평점 API
    @GetMapping("/{id}/average-rating")
    public Object getAverageRating(@PathVariable Long id) {
        double avg = dramaService.getAverageRating(id);
        return success(avg);
    }
} 