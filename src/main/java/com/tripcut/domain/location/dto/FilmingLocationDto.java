package com.tripcut.domain.location.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FilmingLocationDto {
    private Long id;
    private String name;
    private String address;
    private String description;
    private String sceneDescription;
    private Double latitude;
    private Double longitude;
    private Long dramaId;
    private String dramaTitle;
    private Double averageRating;
    private Integer reviewCount;
    private List<String> images;
    private List<String> tags;
    private List<String> stampTypes;
}
