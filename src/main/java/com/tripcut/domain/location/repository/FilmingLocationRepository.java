package com.tripcut.domain.location.repository;

import com.tripcut.domain.location.entity.FilmingLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FilmingLocationRepository extends JpaRepository<FilmingLocation, Long> {
    
    List<FilmingLocation> findByDramaId(Long dramaId);
    
    @Query("SELECT A FROM FilmingLocation A WHERE A.name LIKE %:keyword% OR A.description LIKE %:keyword% OR A.sceneDescription LIKE %:keyword%")
    List<FilmingLocation> findByKeyword(@Param("keyword") String keyword);
    
    @Query("SELECT A FROM FilmingLocation A WHERE A.address LIKE %:address%")
    List<FilmingLocation> findByAddressContaining(@Param("address") String address);
    
    @Query("SELECT A FROM FilmingLocation A WHERE " +
           "6371 * acos(cos(radians(:latitude)) * cos(radians(A.latitude)) * " +
           "cos(radians(A.longitude) - radians(:longitude)) + " +
           "sin(radians(:latitude)) * sin(radians(A.latitude))) <= :radius")
    List<FilmingLocation> findByLocationWithinRadius(
            @Param("latitude") Double latitude,
            @Param("longitude") Double longitude,
            @Param("radius") Double radius);
    
    @Query("SELECT A FROM FilmingLocation A JOIN A.tags tag WHERE tag IN :tags")
    List<FilmingLocation> findByTags(@Param("tags") List<String> tags);
    
    @Query("SELECT A FROM FilmingLocation A ORDER BY (SELECT AVG(B.rating) FROM LocationReview B WHERE B.filmingLocation = B) DESC")
    List<FilmingLocation> findAllOrderByAverageRating();
} 