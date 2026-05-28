package com.ids.infrastructure.persistence.adapter.usr;

import com.ids.domain.entity.usr.UsrUser;
import com.ids.domain.repository.usr.UsrUserRepository;
import com.ids.infrastructure.persistence.mapper.usr.UserPersistenceMapper;
import com.ids.infrastructure.persistence.repository.usr.UsrUserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UsrUserRepositoryAdapter implements UsrUserRepository {

    private final UsrUserJpaRepository usrUserJpaRepository;

    @Override
    public Optional<UsrUser> findByUsernameIgnoreCase(String username) {
        return usrUserJpaRepository.findByUsernameIgnoreCase(username)
                .map(UserPersistenceMapper.UserPersistenceMapStructMapper.INSTANCE::toDomain);
    }

    @Override
    public boolean existsByUsernameIgnoreCase(String username) {
        return usrUserJpaRepository.existsByUsernameIgnoreCase(username);
    }
}