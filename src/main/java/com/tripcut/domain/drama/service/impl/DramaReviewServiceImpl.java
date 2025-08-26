package com.tripcut.domain.drama.service.impl;

import com.tripcut.domain.drama.dto.DramaReviewCreateRequest;
import com.tripcut.domain.drama.dto.DramaReviewUpdateRequest;
import com.tripcut.domain.drama.entity.Drama;
import com.tripcut.domain.drama.entity.DramaReview;
import com.tripcut.domain.drama.repository.DramaRepository;
import com.tripcut.domain.drama.repository.DramaReviewRepository;
import com.tripcut.domain.drama.service.DramaReviewService;
import com.tripcut.domain.user.entity.User;
import com.tripcut.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class DramaReviewServiceImpl implements DramaReviewService {
    private final DramaReviewRepository dramaReviewRepository;
    private final UserRepository userRepository;
    private final DramaRepository dramaRepository;

    @Override
    public DramaReview create(Long dramaId, Long userId, DramaReviewCreateRequest dramaReviewCreateRequest) {
        Drama drama = dramaRepository.findById(dramaId)
                .orElseThrow(() -> new NoSuchElementException("드라마를 찾을 수 없습니다. id=" + dramaId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("사용자를 찾을 수 없습니다. id=" + userId));
        // 중복 리뷰 방지 (원치 않으면 제거)
        if (dramaReviewRepository.existsByDrama_IdAndUser_Id(dramaId, userId)) {
            throw new IllegalStateException("이미 해당 드라마에 리뷰를 작성했습니다.");
        }

        DramaReview dramaReview = new DramaReview();
        dramaReview.setUser(user);
        dramaReview.setDrama(drama);
        dramaReview.setTitle(dramaReviewCreateRequest.getTitle());
        dramaReview.setContent(dramaReviewCreateRequest.getContent());
        dramaReview.setRating(dramaReviewCreateRequest.getRating());

        return dramaReviewRepository.save(dramaReview);
    }

    @Override
    public DramaReview update(Long reviewId, Long userId, DramaReviewUpdateRequest dramaReviewUpdateRequest) {
        DramaReview dramaReview = dramaReviewRepository.findById(reviewId)
                .orElseThrow(() -> new NoSuchElementException("리뷰를 찾을 수 없습니다. id=" + reviewId));

        if (!dramaReview.getUser().getId().equals(userId)) {
            throw new SecurityException("본인의 리뷰만 수정할 수 있습니다.");
        }
        // 중복 리뷰 방지 (원치 않으면 제거)
        dramaReview.setTitle(dramaReviewUpdateRequest.getTitle());
        dramaReview.setContent(dramaReviewUpdateRequest.getContent());
        dramaReview.setRating(dramaReviewUpdateRequest.getRating());

        return dramaReviewRepository.save(dramaReview);
    }

    @Override
    public void delete(Long reviewId, Long userId) {
        DramaReview dramaReview = dramaReviewRepository.findById(reviewId)
                .orElseThrow(() -> new NoSuchElementException("리뷰를 찾을 수 없습니다. id=" + reviewId));

        if (!dramaReview.getUser().getId().equals(userId)) {
            throw new SecurityException("본인의 리뷰만 삭제할 수 있습니다.");
        }
        dramaReviewRepository.delete(dramaReview);

    }
}
