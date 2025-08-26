package com.tripcut.domain.stamp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor
public class StampCreateRequest {
    private String collectedAt;

    private String stampImage;

    private String stampType;

    private String stampDescription;

    private Integer stampPoints;

    private Long filmingLocationId;

    private Long userId;
}
