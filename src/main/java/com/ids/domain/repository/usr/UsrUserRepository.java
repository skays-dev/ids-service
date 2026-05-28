package com.ids.domain.repository.usr;

import com.ids.domain.entity.usr.UsrUser;

import java.util.Optional;

public interface UsrUserRepository {
    Optional<UsrUser> findByUsernameIgnoreCase(String username);
    boolean existsByUsernameIgnoreCase(String username);
}
