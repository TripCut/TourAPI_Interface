package com.tripcut.domain.user.repository;

import java.util.Optional;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;

import com.tripcut.domain.user.entity.User;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u from User u where u.email = :email")
    Optional<User> findByEmail(@Param("email") String email);
    boolean existsByEmail(String email);

    Optional<User> findOneWithAuthoritiesByMemberId(String memberId);
} 