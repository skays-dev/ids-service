package com.ids.application.usecase.usr;

import com.ids.application.dto.usr.CurrentUserDto;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class GetCurrentUserUseCase {
    public CurrentUserDto execute(Authentication authentication) {
        return new CurrentUserDto(
                authentication.getName(),
                authentication.getAuthorities().stream().map(Object::toString).sorted().toList()
        );
    }
}
