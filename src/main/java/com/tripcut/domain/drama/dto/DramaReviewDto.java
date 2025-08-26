package com.tripcut.domain.drama.dto;

import com.tripcut.domain.drama.entity.Drama;
import com.tripcut.domain.user.entity.User;

import java.time.LocalDateTime;

public class DramaReviewDto {
        private Long id;
        private Drama drama;
        private User user;
        private String title;
        private String content;
        private Integer rating; // 1-5점
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
}
