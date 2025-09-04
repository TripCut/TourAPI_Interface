package com.tripcut.domain.filmingLocation.controller;

import com.tripcut.core.controller.BaseController;
import com.tripcut.domain.filmingLocation.dto.FilmingLocationCreateRequest;
import com.tripcut.domain.filmingLocation.dto.FilmingLocationDto;
import com.tripcut.domain.filmingLocation.dto.FilmingLocationUpdateRequest;
import com.tripcut.domain.filmingLocation.service.FilmingLocationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.tripcut.global.common.api.ApiPath.BASE_URL;

@RestController
@RequiredArgsConstructor
@RequestMapping(BASE_URL + "/filmingLocation")
public class FilmingLocationController extends BaseController {

    private final FilmingLocationService filmingLocationService;

    /** 생성 */
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody FilmingLocationCreateRequest req) {
        filmingLocationService.create(req);
        return ResponseEntity.ok("등록 완료");
    }

    /** 단건 조회 */
    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Long id) {
        return ResponseEntity.ok(filmingLocationService.get(id));
    }

    @GetMapping
    public ResponseEntity<Page<FilmingLocationDto>> list(
            @RequestParam(required = false) Long dramaId,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String tag,
            Pageable pageable
    ) {
        return ResponseEntity.ok(filmingLocationService.list(dramaId, keyword, tag, pageable));
    }

    /** 수정 */
    @PutMapping("/{id}")
    public ResponseEntity<FilmingLocationDto> update(
            @PathVariable Long id,
            @Valid @RequestBody FilmingLocationUpdateRequest filmingLocationUpdateRequest
    ) {
        return ResponseEntity.ok(filmingLocationService.update(id, filmingLocationUpdateRequest));
    }

    /** 삭제 */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        filmingLocationService.delete(id);
        return ResponseEntity.ok("삭제 완료");
    }
}
