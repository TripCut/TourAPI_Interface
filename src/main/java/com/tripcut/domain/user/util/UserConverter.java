package com.tripcut.domain.user.util;

import com.tripcut.domain.user.dto.response.UserResponseDto;
import com.tripcut.domain.user.entity.User;

public class UserConverter {
    public static UserResponseDto.JoinResultDTO toJoinResultDTO(User user) {
        return UserResponseDto.JoinResultDTO.builder()
                .userId(user.getId())
                .name(user.getName())
                .email(user.getEmail())

                .build();
    }

}
