package com.tripcut.domain.location.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocationReviewUpdateRequest {
    private String title;
    private String content;
    private Integer rating;
}
