package com.tripcut.domain.user.dto;

import com.tripcut.domain.user.entity.User;
import lombok.*;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberDto {
    private String memberId;
    private String memberPw;
    private String name;
    private String email;
    private String role;
    private String preferredLanguage;
    private String preferredGenres;
    private String badges;
    private Set<String> authorities;


    public static MemberDto from(User user) {
        if (user == null) return null;

        return MemberDto.builder()
                .memberId(user.getMemberId())
                .memberPw(user.getMemberPw())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .preferredLanguage(user.getPreferredLanguage())
                .preferredGenres(user.getPreferredGenres())
                .badges(user.getBadges())
                .build();
    }
}

