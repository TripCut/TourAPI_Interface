package com.tripcut.domain.drama.controller;

import com.tripcut.domain.drama.dto.DramaCreateRequest;
import com.tripcut.domain.drama.dto.DramaDto;
import com.tripcut.domain.drama.dto.DramaUpdateRequest;
import com.tripcut.domain.drama.service.DramaService;
import com.tripcut.domain.drama.util.PaginationSort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dramas")
public class DramaController {

    private final DramaService dramaService;
    private final PaginationSort paginationSort;

    @PostMapping
    private ResponseEntity<?> createDrama(@RequestBody DramaCreateRequest createDramaDto){
       dramaService.create(createDramaDto);
       return ResponseEntity.ok("등록 완료");
    }

    @PutMapping("/{id}")
    private ResponseEntity<?> update(@PathVariable Long id,
                                     @RequestBody DramaUpdateRequest dramaUpdateRequest) {
        return ResponseEntity.ok(dramaService.update(id,dramaUpdateRequest));
    }

    @GetMapping("/{id}")
    private ResponseEntity<?> read(@PathVariable Long id) {
        return ResponseEntity.ok(dramaService.read(id));
    }

    @GetMapping
    public ResponseEntity<Page<DramaDto>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String sort) {

            Pageable pageable = PageRequest.of(page, size, paginationSort.parseSort(sort));
            return ResponseEntity.ok(dramaService.dramaPage(pageable));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        dramaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
