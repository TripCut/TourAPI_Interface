package com.tripcut.domain.admin.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "TBL_ADMIN")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String password;

    private String email;
}
