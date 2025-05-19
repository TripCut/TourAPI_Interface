package com.tripcut.domain.location.entity;

import java.util.ArrayList;
import java.util.List;

import com.tripcut.domain.drama.entity.Drama;

import com.tripcut.domain.stamp.entity.Stamp;
import jakarta.persistence.CascadeType;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class FilmingLocation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String address;
    private String description;
    private String sceneDescription;
    private Double latitude;
    private Double longitude;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "drama_id")
    private Drama drama;
    
    @OneToMany(mappedBy = "filmingLocation", cascade = CascadeType.ALL)
    private List<LocationReview> reviews = new ArrayList<>();
    
    @OneToMany(mappedBy = "filmingLocation", cascade = CascadeType.ALL)
    private List<Stamp> stamps = new ArrayList<>();
    
    @ElementCollection
    private List<String> images = new ArrayList<>();
    
    @ElementCollection
    private List<String> tags = new ArrayList<>();
} 