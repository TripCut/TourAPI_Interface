package com.tripcut.domain.filmingLocation.repository;

import com.tripcut.domain.filmingLocation.entity.FilmingLocation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FilmingLocationRepository extends JpaRepository<FilmingLocation, Long> {

    Page<FilmingLocation> findByDrama_Id(Long dramaId, Pageable pageable);

    Page<FilmingLocation> findByNameContainingIgnoreCase(String keyword, Pageable pageable);

    @Query("""
           select distinct f
           from FilmingLocation f
             join f.tags t
           where lower(t) = lower(:tag)
           """)
    Page<FilmingLocation> findByTag(String tag, Pageable pageable);

    @Query("SELECT A FROM FilmingLocation A WHERE A.name LIKE %:keyword% OR A.description LIKE %:keyword% OR A.sceneDescription LIKE %:keyword%")
    List<FilmingLocation> findByKeyword(@Param("keyword") String keyword);
    
    @Query("SELECT A FROM FilmingLocation A WHERE A.address LIKE %:address%")
    List<FilmingLocation> findByAddressContaining(@Param("address") String address);
    
    @Query("SELECT A FROM FilmingLocation A WHERE " +
           "6371 * acos(cos(radians(:latitude)) * cos(radians(A.lat)) * " +
           "cos(radians(A.lng) - radians(:longitude)) + " +
           "sin(radians(:latitude)) * sin(radians(A.lat))) <= :radius")
    List<FilmingLocation> findByLocationWithinRadius(
            @Param("latitude") Double latitude,
            @Param("longitude") Double longitude,
            @Param("radius") Double radius);
    
    @Query("SELECT A FROM FilmingLocation A JOIN A.tags tag WHERE tag IN :tags")
    List<FilmingLocation> findByTags(@Param("tags") List<String> tags);

    @Query("""
    SELECT A
    FROM FilmingLocation A
    ORDER BY (
        SELECT COALESCE(AVG(B.rating), 0)
        FROM LocationReview B
        WHERE B.filmingLocation = A
    ) DESC
""")
    List<FilmingLocation> findAllOrderByAverageRating();
} 