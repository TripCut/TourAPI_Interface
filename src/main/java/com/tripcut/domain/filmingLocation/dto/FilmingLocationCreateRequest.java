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
public class FilmingLocationCreateRequest {
    private String name;
    private String address;
    private String description;
    private String sceneDescription;
    private Double lat;
    private Double lng;

    private Long dramaId;

    private List<String> images;
    private List<String> tags;
}