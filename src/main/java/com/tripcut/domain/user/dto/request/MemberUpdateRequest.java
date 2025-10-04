package com.tripcut.domain.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberUpdateRequest {
    private String memberPw;
    private String name;
    private String preferredLanguage;
    private String preferredGenres;
    private String badges;
}
