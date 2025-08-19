package com.tripcut.domain.drama.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DramaReviewDto {
    private Long id;
    private Long dramaId;
    private Long userId;
    private String userUsername;
    private String title;
    private String content;
    private Integer rating;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
