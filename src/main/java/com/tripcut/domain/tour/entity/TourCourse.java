package com.tripcut.domain.tour.entity;

import java.util.ArrayList;
import java.util.List;

import com.tripcut.domain.filmingLocation.entity.FilmingLocation;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class TourCourse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private Integer estimatedDuration; // 예상 소요 시간(분)
    private Double totalDistance; // 총 거리(km)
    
    @ManyToMany
    @JoinTable(
        name = "tour_course_locations",
        joinColumns = @JoinColumn(name = "tour_course_id"),
        inverseJoinColumns = @JoinColumn(name = "location_id")
    )
    private List<FilmingLocation> locations = new ArrayList<>();
    
    @ElementCollection
    private List<String> transportationInfo = new ArrayList<>();
    
    @ElementCollection
    private List<String> recommendedRestaurants = new ArrayList<>();
    
    @ElementCollection
    private List<String> recommendedAccommodations = new ArrayList<>();
    
    @ElementCollection
    private List<String> tags = new ArrayList<>();
} 