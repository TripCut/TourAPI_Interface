package com.tripcut.domain.location.controller;

import com.tripcut.core.controller.BaseController;
import com.tripcut.domain.location.dto.*;
import com.tripcut.domain.location.service.LocationReviewService;
import com.tripcut.domain.user.entity.User;
import com.tripcut.domain.user.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import static com.tripcut.global.common.api.ApiPath.BASE_URL;

@RestController
@RequiredArgsConstructor
@RequestMapping(BASE_URL + "/locations")
public class LocationReviewController extends BaseController {

    private final LocationReviewService reviewService;
    private final UserRepository userRepository;

    /** 리뷰 생성 */
    @PostMapping("/{locationId}/reviews")
    public ResponseEntity<LocationReviewDto> create(
            @PathVariable Long locationId,
            @Valid @RequestBody LocationReviewCreateRequest req,
            Authentication authentication
    ) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }
        String username = authentication.getName(); // 보통 email
        Long authUserId = userRepository.findByEmail(username)
                .map(User::getId)
                .orElseThrow(() -> new IllegalStateException("사용자 정보를 찾을 수 없습니다."));

        return ResponseEntity.ok(reviewService.create(locationId, authUserId, req));
    }

    @GetMapping("/{locationId}/reviews")
    public ResponseEntity<Page<LocationReviewDto>> listByLocation(
            @PathVariable Long locationId,
            Pageable pageable
    ) {
        return ResponseEntity.ok(reviewService.listByLocation(locationId, pageable));
    }

    /** 내 리뷰 목록 */
    @GetMapping("/reviews/me")
    public ResponseEntity<Page<LocationReviewDto>> listMine(
            Authentication authentication,
            Pageable pageable
    ) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }
        String username = authentication.getName();
        Long authUserId = userRepository.findByEmail(username)
                .map(User::getId)
                .orElseThrow(() -> new IllegalStateException("사용자 정보를 찾을 수 없습니다."));

        return ResponseEntity.ok(reviewService.listMine(authUserId, pageable));
    }

    /** 단건 조회 */
    @GetMapping("/reviews/{reviewId}")
    public ResponseEntity<LocationReviewDto> get(@PathVariable Long reviewId) {
        return ResponseEntity.ok(reviewService.get(reviewId));
    }

    /** 수정 (본인만) */
    @PutMapping("/reviews/{reviewId}")
    public ResponseEntity<LocationReviewDto> update(
            @PathVariable Long reviewId,
            @Valid @RequestBody LocationReviewUpdateRequest req,
            Authentication authentication
    ) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }
        String username = authentication.getName();
        Long authUserId = userRepository.findByEmail(username)
                .map(User::getId)
                .orElseThrow(() -> new IllegalStateException("사용자 정보를 찾을 수 없습니다."));

        return ResponseEntity.ok(reviewService.update(reviewId, authUserId, req));
    }

    /** 삭제 (본인만) */
    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<Void> delete(
            @PathVariable Long reviewId,
            Authentication authentication
    ) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }
        String username = authentication.getName();
        Long authUserId = userRepository.findByEmail(username)
                .map(User::getId)
                .orElseThrow(() -> new IllegalStateException("사용자 정보를 찾을 수 없습니다."));

        reviewService.delete(reviewId, authUserId);
        return ResponseEntity.noContent().build();
    }
}
