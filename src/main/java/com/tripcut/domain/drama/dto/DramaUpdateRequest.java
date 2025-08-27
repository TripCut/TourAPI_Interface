package com.tripcut.domain.drama.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DramaUpdateRequest {
    private String title;
    private String description;
    private String genre;
    private String broadcastYear;
    private String broadcastStation;
    // null이면 촬영지 변경 안 함, 빈 리스트면 전부 삭제
    private List<FilmingLocationPayload> filmingLocations;
    private List<String> tags;

    @Data
    public static class FilmingLocationPayload {
        private String name;
        private String address;
        private Double lat;
        private Double lng;
    }
}
