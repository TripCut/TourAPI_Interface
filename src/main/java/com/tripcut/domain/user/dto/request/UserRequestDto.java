package com.tripcut.domain.user.dto.request;

import lombok.Getter;

public class UserRequestDto {
    @Getter
    public static class JoinDTO {
        private String name;
        private String email;
        private String password;
        private String role;
    }

    @Getter
    public static class UpdateUserDTO {
        private String name;
    }
}