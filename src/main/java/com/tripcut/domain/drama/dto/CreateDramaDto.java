package com.tripcut.domain.drama.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateDramaDto {
    private String title;
    private String description;
    private String genre;
    private String broadcastYear;
    private String broadcastStation;
}
