package com.tripcut.domain.location.repository;

import com.tripcut.domain.location.entity.LocationReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationReviewRepository extends JpaRepository<LocationReview, Long> {

    Page<LocationReview> findByFilmingLocation_Id(Long locationId, Pageable pageable);
    Page<LocationReview> findByUser_Id(Long userId, Pageable pageable);
    
    @Query("SELECT AVG(A.rating) FROM LocationReview A WHERE A.filmingLocation.id = :locationId")
    Double getAverageRatingByLocationId(@Param("locationId") Long locationId);
    
    @Query("SELECT COUNT(A) FROM LocationReview A WHERE A.filmingLocation.id = :locationId")
    Double getReviewCountByLocationId(@Param("locationId") Long locationId);
} 