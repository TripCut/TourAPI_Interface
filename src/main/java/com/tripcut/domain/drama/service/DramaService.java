package com.tripcut.domain.drama.service;

import com.tripcut.domain.drama.entity.Drama;
import com.tripcut.domain.user.entity.User;
import java.util.List;

public interface DramaService {
    List<Drama> getAllDramas();
    Drama getDramaById(Long id);
    Drama createDrama(Drama drama);
    void deleteDrama(Long id);

    // 비즈니스 로직
    List<Drama> searchDramas(String keyword, String genre, String station);
    double getAverageRating(Long dramaId);
    List<Drama> recommendDramasForUser(User user);
}
