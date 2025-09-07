package com.tripcut.domain.drama.controller;

import com.tripcut.core.controller.BaseController;
import com.tripcut.domain.drama.dto.DramaReviewDto;
import com.tripcut.domain.drama.dto.DramaReviewUpdateRequest;
import com.tripcut.domain.user.entity.User;
import com.tripcut.domain.drama.dto.DramaReviewCreateRequest;
import com.tripcut.domain.drama.service.DramaReviewService;
import com.tripcut.domain.user.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import static com.tripcut.global.common.api.ApiPath.BASE_URL;

@RestController
@RequiredArgsConstructor
@RequestMapping(BASE_URL + "/dramaReviews")
public class DramaReviewController extends BaseController {

    private final DramaReviewService dramaReviewService;
    private final UserRepository userRepository;

    @PostMapping("/{dramaId}/reviews")
    public ResponseEntity<?> create(
            @PathVariable Long dramaId,
            @Valid @RequestBody DramaReviewCreateRequest request,
            Authentication authentication
    ) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body("로그인이 필요합니다.");
        }
        String username = authentication.getName();
        Long user = userRepository.findByEmail(username)
                .map(User::getId)
                .orElseThrow(() -> new IllegalStateException("사용자 정보를 찾을 수 없습니다."));
        System.out.println("user :" + user);
        dramaReviewService.create(dramaId, user, request);

        return ResponseEntity.ok("등록 완료");
    }
    @PutMapping("/reviews/{reviewId}")
    public ResponseEntity<?> update(
            @PathVariable Long reviewId,
            @Valid @RequestBody DramaReviewUpdateRequest req,
            @RequestParam Long userId // 시큐리티 적용 시 본인 ID로 대체
    ) {
        dramaReviewService.update(reviewId, userId, req);
        return ResponseEntity.ok("수정 완료");
    }

    /** 삭제 */
    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<?> delete(
            @PathVariable Long reviewId,
            @RequestParam Long userId // 시큐리티 적용 시 본인 ID로 대체
    ) {
        dramaReviewService.delete(reviewId, userId);
        return ResponseEntity.ok("삭제 완료");
    }
}
