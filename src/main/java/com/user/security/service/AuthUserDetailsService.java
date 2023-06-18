package com.user.security.service;

import com.user.security.core.UserDetailsImpl;
import com.user.security.entity.AuthUser;
import com.user.security.repository.AuthUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthUserDetailsService implements UserDetailsService {

    private final AuthUserRepository authUserRepository;

    public UserDetails loadUserByUsername(final String userName) throws UsernameNotFoundException {
        Optional<AuthUser> authUserOptional = Optional.ofNullable(authUserRepository.findByUserName(userName))
                .orElseThrow(() -> new UsernameNotFoundException("No User present in Authentication entity with name : " + userName));
        return UserDetailsImpl.build(authUserOptional.get());
    }

}
