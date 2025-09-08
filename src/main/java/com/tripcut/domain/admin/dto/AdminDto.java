package com.tripcut.domain.admin.dto;

import com.tripcut.domain.admin.entity.Admin;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminDto {

    private Long id;
    private String username;
    private String email;

    public static AdminDto from(Admin admin) {
        if (admin == null) {
            return null;
        }
        return AdminDto.builder()
                .id(admin.getId())
                .username(admin.getUsername())
                .email(admin.getEmail())
                .build();
    }
}
