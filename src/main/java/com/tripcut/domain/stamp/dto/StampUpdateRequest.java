package com.tripcut.domain.stamp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor
public class StampUpdateRequest {
    private String collectedAt;

    private String stampImage;

    private String stampType;

    private String stampDescription;

    private Integer stampPoints;

}

