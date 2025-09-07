package com.tripcut.domain.drama.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class DramaReviewDto {
        private Long id;
        private Long dramaId;
        private Long userId;
        private String title;
        private String content;
        private Integer rating; // 1-5점
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
}
