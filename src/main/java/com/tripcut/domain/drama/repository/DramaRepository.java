package com.tripcut.domain.drama.repository;

import com.tripcut.domain.drama.entity.Drama;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DramaRepository extends JpaRepository<Drama, Long> {
    List<Drama> findByTitleContainingIgnoreCase(String keyword);
    List<Drama> findByGenre(String genre);
    List<Drama> findByBroadcastStation(String station);

    // 동적 검색
    @Query("SELECT d FROM Drama d WHERE " +
           "(:keyword IS NULL OR LOWER(d.title) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
           "(:genre IS NULL OR d.genre = :genre) AND " +
           "(:station IS NULL OR d.broadcastStation = :station)")
    List<Drama> searchDramas(@Param("keyword") String keyword,
                             @Param("genre") String genre,
                             @Param("station") String station);

    // 평균 평점 집계
    @Query("SELECT AVG(r.rating) FROM DramaReview r WHERE r.drama.id = :dramaId")
    Double getAverageRating(@Param("dramaId") Long dramaId);

    // 인기 드라마 TOP N개 (리뷰 많은 순)
    @Query("SELECT d FROM Drama d LEFT JOIN d.reviews r GROUP BY d.id ORDER BY COUNT(r) DESC")
    List<Drama> findTopDramasByReviewCount(Pageable pageable);
} 