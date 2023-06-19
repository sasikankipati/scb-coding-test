package com.user.security.service;

import com.user.security.core.ClientDetailsImpl;
import com.user.security.entity.Client;
import com.user.security.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthClientDetailsService implements UserDetailsService {

    private final ClientRepository clientRepository;

    public UserDetails loadUserByUsername(final String clientName) throws UsernameNotFoundException {
        Optional<Client> authUserOptional = Optional.ofNullable(clientRepository.findByClientName(clientName))
                .orElseThrow(() -> new UsernameNotFoundException("No User present in Authentication entity with name : " + clientName));
        return ClientDetailsImpl.build(authUserOptional.get());
    }

}
