package com.tripcut.domain.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


public class UserResponseDto {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class JoinResultDTO {
        private Long userId;
        private String name;
        private String email;
        private LocalDateTime createAt;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class UserPreviewDTO {
        private Long userId;
        private String name;
        private LocalDateTime updateAt;
        private LocalDateTime createAt;
    }

}
