package com.tripcut.domain.drama.service.impl;

import com.tripcut.domain.drama.entity.Drama;
import com.tripcut.domain.drama.repository.DramaRepository;
import com.tripcut.domain.drama.service.DramaService;
import com.tripcut.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DramaServiceImpl implements DramaService {
    private final DramaRepository dramaRepository;

    @Override
    public List<Drama> getAllDramas() {
        return dramaRepository.findAll();
    }

    @Override
    public Drama getDramaById(Long id) {
        return dramaRepository.findById(id).orElse(null);
    }

    @Override
    public Drama createDrama(Drama drama) {
        return dramaRepository.save(drama);
    }

    @Override
    public void deleteDrama(Long id) {
        dramaRepository.deleteById(id);
    }

    // 비즈니스 로직 구현
    @Override
    public List<Drama> searchDramas(String keyword, String genre, String station) {
        // 단순 조합 검색 (실제 서비스에서는 QueryDSL/Specification으로 동적 쿼리 추천)
        List<Drama> result = new ArrayList<>();
        if (keyword != null && !keyword.isEmpty()) {
            result.addAll(dramaRepository.findByTitleContainingIgnoreCase(keyword));
        } else if (genre != null && !genre.isEmpty()) {
            result.addAll(dramaRepository.findByGenre(genre));
        } else if (station != null && !station.isEmpty()) {
            result.addAll(dramaRepository.findByBroadcastStation(station));
        } else {
            result.addAll(dramaRepository.findAll());
        }
        return result;
    }

    @Override
    public double getAverageRating(Long dramaId) {
        Drama drama = dramaRepository.findById(dramaId).orElse(null);
        if (drama == null || drama.getReviews().isEmpty()) return 0.0;
        return drama.getReviews().stream()
                .mapToInt(r -> r.getRating() != null ? r.getRating() : 0)
                .average().orElse(0.0);
    }

    @Override
    public List<Drama> recommendDramasForUser(User user) {
        // 선호 장르 기반 추천 
        if (user == null || user.getPreferredGenres() == null || user.getPreferredGenres().isEmpty()) {
            return dramaRepository.findAll();
        }
        List<Drama> result = new ArrayList<>();
        for (String genre : user.getPreferredGenres()) {
            result.addAll(dramaRepository.findByGenre(genre));
        }
        return result;
    }
}
