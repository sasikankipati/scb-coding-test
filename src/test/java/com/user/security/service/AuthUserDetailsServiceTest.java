package com.user.security.service;

import com.user.common.AuthenticationTestSupport;
import com.user.security.repository.AuthUserRepository;
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
public class AuthUserDetailsServiceTest {

    @InjectMocks
    AuthUserDetailsService authUserDetailsService;

    @Mock
    AuthUserRepository authUserRepository;

    @Test
    public void testLoadUserByUsernameSuccess() {
        Mockito.when(authUserRepository.findByUserName("UserName")).thenReturn(
                Optional.of(AuthenticationTestSupport.prepareAuthUser()));
        UserDetails userDetails = authUserDetailsService.loadUserByUsername("UserName");
        Assertions.assertEquals("UserName", userDetails.getUsername());
    }

    @Test
    public void testLoadUserByUsernameFailure() {
        Mockito.when(authUserRepository.findByUserName("UserName")).thenReturn(
                null);
        Exception exception = Assertions.assertThrows(UsernameNotFoundException.class, () -> {
            authUserDetailsService.loadUserByUsername("UserName");
        });
        Assertions.assertEquals("No User present in Authentication entity with name : UserName", exception.getMessage());
    }
}
