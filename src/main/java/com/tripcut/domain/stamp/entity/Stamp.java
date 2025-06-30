package com.tripcut.domain.stamp.entity;

import com.tripcut.domain.location.entity.FilmingLocation;
import com.tripcut.domain.user.entity.User;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Stamp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String collectedAt;
    private String stampImage;
    private String stampType;
    private String stampDescription;
    private Integer stampPoints;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private FilmingLocation filmingLocation;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
} 