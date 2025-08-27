package com.tripcut.domain.drama.entity;

import java.util.ArrayList;
import java.util.List;

import com.tripcut.domain.filmingLocation.entity.FilmingLocation;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class Drama {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private String genre;
    private String broadcastYear;
    private String broadcastStation;
    
    @OneToMany(mappedBy = "drama", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FilmingLocation> filmingLocations = new ArrayList<>();
    
    @OneToMany(mappedBy = "drama", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DramaReview> reviews = new ArrayList<>();

    public void addFilmingLocation(FilmingLocation loc) {
        filmingLocations.add(loc);
        loc.setDrama(this);
    }
    public void removeFilmingLocation(FilmingLocation loc) {
        filmingLocations.remove(loc);
        loc.setDrama(null);
    }
    public void clearFilmingLocations() {
        for (FilmingLocation loc : new ArrayList<>(filmingLocations)) {
            removeFilmingLocation(loc);
        }
    }


} 