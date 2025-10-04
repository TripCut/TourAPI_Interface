package com.tripcut.domain.drama.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tripcut.core.controller.BaseController;
import com.tripcut.domain.drama.dto.DramaCreateRequest;
import com.tripcut.domain.drama.dto.DramaDto;
import com.tripcut.domain.drama.dto.DramaUpdateRequest;
import com.tripcut.domain.drama.service.DramaService;
import com.tripcut.domain.drama.util.PaginationSort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.tripcut.global.common.api.ApiPath.BASE_URL;

@RestController
@RequiredArgsConstructor
@RequestMapping(BASE_URL + "/dramas")
public class DramaController extends BaseController {

    private final DramaService dramaService;
    private final PaginationSort paginationSort;



    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createDrama(
            @RequestPart("request") String requestJson,  // 예시 입력값 README 참고
            @RequestPart(value = "image", required = false) MultipartFile posterFile
    ) throws IOException {
        DramaCreateRequest request = new ObjectMapper().readValue(requestJson, DramaCreateRequest.class);
        DramaDto created = dramaService.create(request, posterFile);
        return ResponseEntity.ok(created);
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
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String title) {

            Pageable pageable = PageRequest.of(page, size, paginationSort.parseSort(sort));
            if (title != null && !title.isBlank()) {
                return ResponseEntity.ok(dramaService.searchByTitle(title, pageable));
            }
            return ResponseEntity.ok(dramaService.dramaPage(pageable));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        dramaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
