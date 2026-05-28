package com.ids.infrastructure.security;

import com.ids.domain.entity.usr.UsrRole;
import com.ids.domain.entity.usr.UsrUser;
import com.ids.domain.repository.usr.UsrUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DatabaseUserDetailsService implements UserDetailsService {
    private final UsrUserRepository userRepository;

    @Override
    @Transactional(readOnly = true, transactionManager = "usersTransactionManager")
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UsrUser user = userRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .disabled(!user.isEnabled())
                .authorities(user.getRoles().stream().map(UsrRole::getName).toArray(String[]::new))
                .build();
    }
}
