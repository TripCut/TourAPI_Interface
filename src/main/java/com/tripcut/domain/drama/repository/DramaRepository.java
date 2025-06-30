package com.tripcut.domain.drama.repository;

import com.tripcut.domain.drama.entity.Drama;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DramaRepository extends JpaRepository<Drama, Long> {
    
    List<Drama> findByGenre(String genre);
    
    List<Drama> findByBroadcastYear(String broadcastYear);
    
    List<Drama> findByBroadcastStation(String broadcastStation);
    
    @Query("SELECT A FROM Drama A WHERE A.title LIKE %:keyword% OR A.description LIKE %:keyword%")
    List<Drama> findByKeyword(@Param("keyword") String keyword);
    
    @Query("SELECT A FROM Drama A JOIN A.filmingLocations B WHERE B.name LIKE %:locationName%")
    List<Drama> findByFilmingLocationName(@Param("locationName") String locationName);
    
    @Query("SELECT A FROM Drama A ORDER BY (SELECT AVG(B.rating) FROM DramaReview B WHERE B.drama = A) DESC")
    List<Drama> findAllOrderByAverageRating();
} 