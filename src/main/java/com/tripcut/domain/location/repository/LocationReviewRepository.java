package com.tripcut.domain.location.repository;

import com.tripcut.domain.location.entity.LocationReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationReviewRepository extends JpaRepository<LocationReview, Long> {
    
    List<LocationReview> findByFilmingLocationId(Long locationId);
    
    List<LocationReview> findByUserId(Long userId);
    
    @Query("SELECT AVG(A.rating) FROM LocationReview A WHERE A.filmingLocation.id = :locationId")
    Double getAverageRatingByLocationId(@Param("locationId") Long locationId);
    
    @Query("SELECT COUNT(A) FROM LocationReview A WHERE A.filmingLocation.id = :locationId")
    Integer getReviewCountByLocationId(@Param("locationId") Long locationId);
} 