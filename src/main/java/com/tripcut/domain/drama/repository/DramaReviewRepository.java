package com.tripcut.domain.drama.repository;

import com.tripcut.domain.drama.entity.DramaReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DramaReviewRepository extends JpaRepository<DramaReview, Long> {
    
    List<DramaReview> findByDramaId(Long dramaId);
    
    List<DramaReview> findByUserId(Long userId);
    
    @Query("SELECT AVG(dr.rating) FROM DramaReview dr WHERE dr.drama.id = :dramaId")
    Double getAverageRatingByDramaId(@Param("dramaId") Long dramaId);
    
    @Query("SELECT COUNT(dr) FROM DramaReview dr WHERE dr.drama.id = :dramaId")
    Integer getReviewCountByDramaId(@Param("dramaId") Long dramaId);
} 