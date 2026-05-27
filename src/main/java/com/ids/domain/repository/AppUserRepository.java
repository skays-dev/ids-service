package com.ids.domain.repository;

import com.ids.domain.entity.AppUser;

import java.util.Optional;

public interface AppUserRepository {
    Optional<AppUser> findByUsernameIgnoreCase(String username);
    boolean existsByUsernameIgnoreCase(String username);
}
