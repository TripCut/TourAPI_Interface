package com.tripcut.domain.drama.util;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class PaginationSort {
    public Sort parseSort(String sortParam) {
        if (sortParam == null || sortParam.isBlank()) return Sort.unsorted();
        try {
            if (sortParam.startsWith("[")) {
                String[] arr = new com.fasterxml.jackson.databind.ObjectMapper()
                        .readValue(sortParam, String[].class);
                String field = arr.length > 0 ? arr[0] : "id";
                String dir   = arr.length > 1 ? arr[1] : "ASC";
                return safeSort(field, dir);
            }
            // 기본 형식 "field,asc"
            String[] parts = sortParam.split(",");
            if (parts.length >= 2) return safeSort(parts[0], parts[1]);
            return safeSort(parts[0], "ASC");
        } catch (Exception e) {
            return Sort.unsorted(); // 실패 시 정렬 미적용
        }
    }

    public Sort safeSort(String field, String dir) {
        // 화이트리스트(엔티티에 실제 존재하는 컬럼만 허용)
        java.util.Set<String> allowed = java.util.Set.of(
                "id", "title", "genre", "broadcastYear", "broadcastStation", "description"
        );
        if (!allowed.contains(field)) return Sort.unsorted();
        return Sort.by(Sort.Direction.fromString(dir), field);
    }
}
