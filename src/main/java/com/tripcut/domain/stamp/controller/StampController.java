package com.tripcut.domain.stamp.controller;// package com.tripcut.domain.stamp.controller;

import com.tripcut.core.controller.BaseController;
import com.tripcut.domain.stamp.dto.StampDto;
import com.tripcut.domain.stamp.service.StampService;
import com.tripcut.domain.user.entity.User;
import com.tripcut.domain.user.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.tripcut.global.common.api.ApiPath.BASE_URL;

@RestController
@RequiredArgsConstructor
@RequestMapping(BASE_URL + "/stamps")
public class StampController extends BaseController {

    private final StampService stampService;
    private final UserRepository userRepository;

    /** 스탬프 수집(생성) */
    @PostMapping
    public ResponseEntity<StampDto> collect(
            @Valid @RequestBody StampDto req,
            Authentication authentication
    ) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }
        String username = authentication.getName(); // 보통 email
        Long authUserId = userRepository.findByEmail(username)
                .map(User::getId)
                .orElseThrow(() -> new IllegalStateException("사용자 정보를 찾을 수 없습니다."));
        return ResponseEntity.ok(stampService.collect(authUserId, req));
    }

    /** 촬영지 기준: 모든 사용자 스탬프 목록 (페이지) */
    @GetMapping("/locations/{filmingLocationId}")
    public ResponseEntity<Page<StampDto>> listByLocation(
            @PathVariable Long filmingLocationId,
            Pageable pageable
    ) {
        return ResponseEntity.ok(stampService.listByLocation(filmingLocationId, pageable));
    }

    /** 내 스탬프: 특정 촬영지 (페이지) */
    @GetMapping("/me/locations/{filmingLocationId}")
    public ResponseEntity<Page<StampDto>> listMineByLocation(
            @PathVariable Long filmingLocationId,
            Pageable pageable,
            Authentication authentication
    ) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }
        String username = authentication.getName();
        Long authUserId = userRepository.findByEmail(username)
                .map(User::getId)
                .orElseThrow(() -> new IllegalStateException("사용자 정보를 찾을 수 없습니다."));
        return ResponseEntity.ok(stampService.listMineByLocation(authUserId, filmingLocationId, pageable));
    }

    /** 내 스탬프: 특정 드라마(리스트) */
    @GetMapping("/me/dramas/{dramaId}")
    public ResponseEntity<List<StampDto>> listMineByDrama(
            @PathVariable Long dramaId,
            Authentication authentication
    ) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }
        String username = authentication.getName();
        Long authUserId = userRepository.findByEmail(username)
                .map(User::getId)
                .orElseThrow(() -> new IllegalStateException("사용자 정보를 찾을 수 없습니다."));
        return ResponseEntity.ok(stampService.listMineByDrama(authUserId, dramaId));
    }

    /** 내 드라마 진행도(수집 개수) */
    @GetMapping("/me/dramas/{dramaId}/count")
    public ResponseEntity<Long> countMineByDrama(
            @PathVariable Long dramaId,
            Authentication authentication
    ) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }
        String username = authentication.getName();
        Long authUserId = userRepository.findByEmail(username)
                .map(User::getId)
                .orElseThrow(() -> new IllegalStateException("사용자 정보를 찾을 수 없습니다."));
        return ResponseEntity.ok(stampService.countMineByDrama(authUserId, dramaId));
    }

    /** 내 총 포인트 */
    @GetMapping("/me/points/total")
    public ResponseEntity<Integer> totalPointsMine(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }
        String username = authentication.getName();
        Long authUserId = userRepository.findByEmail(username)
                .map(User::getId)
                .orElseThrow(() -> new IllegalStateException("사용자 정보를 찾을 수 없습니다."));
        return ResponseEntity.ok(stampService.totalPointsMine(authUserId));
    }

    /** 최근 수집 (기본 10개) */
    @GetMapping("/me/recent")
    public ResponseEntity<List<StampDto>> recentMine(
            @RequestParam(defaultValue = "10") int limit,
            Authentication authentication
    ) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }
        String username = authentication.getName();
        Long authUserId = userRepository.findByEmail(username)
                .map(User::getId)
                .orElseThrow(() -> new IllegalStateException("사용자 정보를 찾을 수 없습니다."));
        return ResponseEntity.ok(stampService.recentMine(authUserId, limit));
    }
}