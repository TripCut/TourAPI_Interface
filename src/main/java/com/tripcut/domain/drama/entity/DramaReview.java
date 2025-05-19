package com.tripcut.domain.drama.entity;

import com.tripcut.domain.user.entity.User;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class DramaReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;
    private Integer rating;
    private String createdAt;
    private String updatedAt;

    @ManyToOne
    @JoinColumn(name = "drama_id")
    private Drama drama;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
} 