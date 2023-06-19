package com.user.security.service;

import com.user.common.AuthenticationTestSupport;
import com.user.security.repository.ClientRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class AuthClientDetailsServiceTest {

    @InjectMocks
    AuthClientDetailsService authClientDetailsService;

    @Mock
    ClientRepository clientRepository;

    @Test
    public void testLoadUserByUsernameSuccess() {
        Mockito.when(clientRepository.findByClientName("ClientName")).thenReturn(
                Optional.of(AuthenticationTestSupport.prepareClient()));
        UserDetails userDetails = authClientDetailsService.loadUserByUsername("ClientName");
        Assertions.assertEquals("ClientName", userDetails.getUsername());
    }

    @Test
    public void testLoadUserByUsernameFailure() {
        Mockito.when(clientRepository.findByClientName("ClientName")).thenReturn(
                null);
        Exception exception = Assertions.assertThrows(UsernameNotFoundException.class, () -> {
            authClientDetailsService.loadUserByUsername("ClientName");
        });
        Assertions.assertEquals("No User present in Authentication entity with name : ClientName", exception.getMessage());
    }
}
