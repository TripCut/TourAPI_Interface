package com.tripcut.domain.drama.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DramaResponseDto {
    private Long id;
    private String title;
    private String description;
    private String genre;
    private String broadcastYear;
    private String broadcastStation;
} 