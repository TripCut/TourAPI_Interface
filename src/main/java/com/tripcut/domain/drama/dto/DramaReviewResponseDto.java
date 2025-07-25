 package com.tripcut.domain.drama.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DramaReviewResponseDto {
    private Long dramaId;
    private Integer rating;
    private String content;
} 