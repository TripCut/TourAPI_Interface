package com.tripcut.domain.drama.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DramaDto {
    private Long id;
    private String title;
    private String description;
    private String genre;
    private String broadcastYear;
    private String broadcastStation;
    private Double averageRating;
    private Integer reviewCount;
    private List<String> filmingLocationNames;
    private List<String> tags;
}
