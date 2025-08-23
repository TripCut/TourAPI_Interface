package com.tripcut.domain.drama.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DramaCreateRequest {
    private String title;
    private String description;
    private String genre;
    private String broadcastYear;
    private String broadcastStation;
    private List<FilmingLocationPayload> filmingLocations;
    private List<String> tags;

    @Getter
    public static class FilmingLocationPayload {
        private String name;
        private String address;
        private Double lat;
        private Double lng;
    }
}
