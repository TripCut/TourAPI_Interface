package com.tripcut.domain.stamp.repository;

import com.tripcut.domain.stamp.entity.Stamp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface StampRepository extends JpaRepository<Stamp, Long> {

    Page<Stamp> findByUser_Id(Long userId, Pageable pageable);

    boolean existsByUser_IdAndFilmingLocation_IdAndStampType(Long userId, Long filmingLocationId, String stampType);

    Page<Stamp> findByFilmingLocation_Id(Long filmingLocationId, Pageable pageable);

    Page<Stamp> findByUser_IdAndFilmingLocation_Id(Long userId, Long filmingLocationId, Pageable pageable);

    @Query("SELECT A FROM Stamp A WHERE A.user.id = :userId AND A.filmingLocation.drama.id = :dramaId")
    List<Stamp> findByUserIdAndDramaId(@Param("userId") Long userId, @Param("dramaId") Long dramaId);
    
    @Query("SELECT COUNT(A) FROM Stamp A WHERE A.user.id = :userId AND A.filmingLocation.drama.id = :dramaId")
    Long getStampCountByUserIdAndDramaId(@Param("userId") Long userId, @Param("dramaId") Long dramaId);
    
    @Query("SELECT SUM(A.stampPoints) FROM Stamp A WHERE A.user.id = :userId")
    Integer getTotalPointsByUserId(@Param("userId") Long userId);
    
    @Query("SELECT A FROM Stamp A WHERE A.user.id = :userId ORDER BY A.collectedAt DESC")
    List<Stamp> findByUserIdOrderByCollectedAtDesc(@Param("userId") Long userId);
}
