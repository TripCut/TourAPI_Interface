package com.tripcut.domain.filmingLocation.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FilmingLocationDto {
    private String name;
    private String address;
    private String description;
    private String sceneDescription;
    private Double lat;
    private Double lng;

    private Long dramaId;
    private String dramaTitle;

    private List<String> images;
    private List<String> tags;
}