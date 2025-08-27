package com.tripcut.domain.filmingLocation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FilmingLocationUpdateRequest {
    private String name;
    private String address;
    private String description;
    private String sceneDescription;
    private Double lat;
    private Double lng;

    // 보통 관계는 수정 잘 안 하지만 필요하면 허용
    private Long dramaId;

    private List<String> images;
    private List<String> tags;
}