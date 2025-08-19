package com.tripcut.domain.drama.entity;

import java.util.ArrayList;
import java.util.List;

import com.tripcut.domain.location.entity.FilmingLocation;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Drama {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private String genre;
    private String broadcastYear;
    private String broadcastStation;
    
    @OneToMany(mappedBy = "drama", cascade = CascadeType.ALL)
    private List<FilmingLocation> filmingLocations = new ArrayList<>();
    
    @OneToMany(mappedBy = "drama", cascade = CascadeType.ALL)
    private List<DramaReview> reviews = new ArrayList<>();
} 