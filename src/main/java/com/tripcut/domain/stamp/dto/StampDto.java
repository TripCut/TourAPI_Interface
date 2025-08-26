package com.tripcut.domain.stamp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StampDto {
    private String collectedAt;
    private String stampImage;
    private String stampType;
    private String stampDescription;
    private Integer stampPoints;
    private Long locationId;
    private String locationName;
    private Long userId;
    private String userUsername;
    private Boolean isLocationValidated;
    private Double userLatitude;
    private Double userLongitude;
    private Double locationLatitude;
    private Double locationLongitude;
}
