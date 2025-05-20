package com.tripcut.domain.user.entity;

import java.util.ArrayList;
import java.util.List;

import com.tripcut.domain.drama.entity.DramaReview;
import com.tripcut.domain.location.entity.LocationReview;
import com.tripcut.domain.stamp.entity.Stamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    private String password;
    private String username;
    private String preferredLanguage;
    
    @ElementCollection
    private List<String> preferredGenres = new ArrayList<>();
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<DramaReview> dramaReviews = new ArrayList<>();
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<LocationReview> locationReviews = new ArrayList<>();
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Stamp> stamps = new ArrayList<>();
    
    @ElementCollection
    private List<String> badges = new ArrayList<>();
} 