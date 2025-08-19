package com.tripcut.domain.drama.controller;

import com.tripcut.domain.drama.dto.CreateDramaDto;
import com.tripcut.domain.drama.service.DramaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/drama")
@RequiredArgsConstructor
public class DramaController {

    private final DramaService dramaService;

    @PostMapping("/")
    private ResponseEntity<?> createDrama(@RequestBody CreateDramaDto createDramaDto){
       dramaService.createDrama(createDramaDto);
       return ResponseEntity.ok("등록 완료");
    }

}
