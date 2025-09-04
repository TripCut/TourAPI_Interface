package com.tripcut.domain.stamp.entity;

import com.tripcut.domain.filmingLocation.entity.FilmingLocation;
import com.tripcut.domain.user.entity.User;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "TBL_STAMP")
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
    @JoinColumn(name = "filming_location_id")
    private FilmingLocation filmingLocation;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
} 