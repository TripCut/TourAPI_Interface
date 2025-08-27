package com.tripcut.domain.drama.dto;

import lombok.Getter;

@Getter
public class DramaReviewUpdateRequest {
    String title;
    String content;
    Integer rating;
}
