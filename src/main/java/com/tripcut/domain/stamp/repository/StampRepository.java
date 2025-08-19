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

    @Query("select s from Stamp s where s.filmingLocation.id = :locationId")
    List<Stamp> findByLocationId(@Param("locationId") Long locationId);

    List<Stamp> findByUser_IdAndFilmingLocation_Id(Long userId, Long locationId);
    
    @Query("SELECT A FROM Stamp A WHERE A.user.id = :userId AND A.filmingLocation.drama.id = :dramaId")
    List<Stamp> findByUserIdAndDramaId(@Param("userId") Long userId, @Param("dramaId") Long dramaId);
    
    @Query("SELECT COUNT(A) FROM Stamp A WHERE A.user.id = :userId AND A.filmingLocation.drama.id = :dramaId")
    Long getStampCountByUserIdAndDramaId(@Param("userId") Long userId, @Param("dramaId") Long dramaId);
    
    @Query("SELECT SUM(A.stampPoints) FROM Stamp A WHERE A.user.id = :userId")
    Integer getTotalPointsByUserId(@Param("userId") Long userId);
    
    @Query("SELECT A FROM Stamp A WHERE A.user.id = :userId ORDER BY A.collectedAt DESC")
    List<Stamp> findByUserIdOrderByCollectedAtDesc(@Param("userId") Long userId);
}
