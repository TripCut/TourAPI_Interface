package com.tripcut.domain.stamp.repository;

import com.tripcut.domain.stamp.entity.Stamp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StampRepository extends JpaRepository<Stamp, Long> {
    
    List<Stamp> findByUserId(Long userId);
    
    List<Stamp> findByLocationId(Long locationId);
    
    List<Stamp> findByUserIdAndLocationId(Long userId, Long locationId);
    
    @Query("SELECT A FROM Stamp A WHERE A.user.id = :userId AND A.filmingLocation.drama.id = :dramaId")
    List<Stamp> findByUserIdAndDramaId(@Param("userId") Long userId, @Param("dramaId") Long dramaId);
    
    @Query("SELECT COUNT(A) FROM Stamp A WHERE A.user.id = :userId AND A.filmingLocation.drama.id = :dramaId")
    Integer getStampCountByUserIdAndDramaId(@Param("userId") Long userId, @Param("dramaId") Long dramaId);
    
    @Query("SELECT SUM(A.stampPoints) FROM Stamp A WHERE A.user.id = :userId")
    Integer getTotalPointsByUserId(@Param("userId") Long userId);
    
    @Query("SELECT A FROM Stamp A WHERE A.user.id = :userId ORDER BY A.collectedAt DESC")
    List<Stamp> findByUserIdOrderByCollectedAtDesc(@Param("userId") Long userId);
}
