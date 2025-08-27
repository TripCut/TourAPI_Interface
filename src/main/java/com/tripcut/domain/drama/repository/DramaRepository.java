package com.tripcut.domain.drama.repository;

import com.tripcut.domain.drama.entity.Drama;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DramaRepository extends JpaRepository<Drama, Long> {
    boolean existsByTitle(String title);
}
