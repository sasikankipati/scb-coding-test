package com.user.security.service;

import com.user.common.AuthenticationTestSupport;
import com.user.common.constants.RoleEnum;
import com.user.security.dto.request.AuthenticationRequest;
import com.user.security.dto.request.RegisterRequest;
import com.user.security.dto.response.AuthenticationResponse;
import com.user.security.dto.response.RegisterResponse;
import com.user.security.entity.Client;
import com.user.security.entity.Role;
import com.user.security.exception.ClientAlreadyExistsException;
import com.user.security.exception.RoleNotFoundException;
import com.user.security.repository.ClientRepository;
import com.user.security.repository.RoleRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {
    
    @InjectMocks
    AuthenticationService authenticationService;

    @Mock
    ClientRepository clientRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    JWTService jwtService;

    @Mock
    AuthenticationManager authenticationManager;

    @Mock
    RoleRepository roleRepository;

    @Test
    public void testRegisterClientSuccess() {
        RegisterRequest registerRequest = AuthenticationTestSupport.prepareRegisterRequest();
        Mockito.when(clientRepository.existsByClientName(registerRequest.getClientName())).thenReturn(false);
        Mockito.when(passwordEncoder.encode(registerRequest.getPassword())).thenReturn("wtertewrewurewur");
        Mockito.when(roleRepository.findByName(RoleEnum.ROLE_USER)).thenReturn(Optional.of(new Role(1, RoleEnum.ROLE_USER)));
        Mockito.when(clientRepository.save(Mockito.any())).thenReturn(new Client());
        RegisterResponse registerResponse = authenticationService.registerClient(registerRequest);
        Assertions.assertEquals("Client registered successfully!", registerResponse.getMessage());
    }

    @Test
    public void testRegisterClientSuccessWithoutRoles() {
        RegisterRequest registerRequest = AuthenticationTestSupport.prepareRegisterRequest();
        registerRequest.setRoles(null);
        Mockito.when(clientRepository.existsByClientName(registerRequest.getClientName())).thenReturn(false);
        Mockito.when(passwordEncoder.encode(registerRequest.getPassword())).thenReturn("wtertewrewurewur");
        Mockito.when(roleRepository.findByName(RoleEnum.ROLE_USER)).thenReturn(Optional.of(new Role(1, RoleEnum.ROLE_USER)));
        Mockito.when(clientRepository.save(Mockito.any())).thenReturn(new Client());
        RegisterResponse registerResponse = authenticationService.registerClient(registerRequest);
        Assertions.assertEquals("Client registered successfully!", registerResponse.getMessage());
    }

    @Test
    public void testRegisterClientFailureOnDuplicateUser() {
        RegisterRequest registerRequest = AuthenticationTestSupport.prepareRegisterRequest();
        Mockito.when(clientRepository.existsByClientName(registerRequest.getClientName())).thenReturn(true);
        Exception exception = Assertions.assertThrows(ClientAlreadyExistsException.class, () -> {
            authenticationService.registerClient(registerRequest);
        });
        Assertions.assertEquals("Client already exists with name : ClientName", exception.getMessage());
    }

    @Test
    public void testRegisterClientFailureOnRoleNotFound() {
        RegisterRequest registerRequest = AuthenticationTestSupport.prepareRegisterRequest();
        Mockito.when(clientRepository.existsByClientName(registerRequest.getClientName())).thenReturn(false);
        Mockito.when(passwordEncoder.encode(registerRequest.getPassword())).thenReturn("wtertewrewurewur");
        Mockito.when(roleRepository.findByName(RoleEnum.ROLE_USER)).thenThrow(new RoleNotFoundException(RoleEnum.ROLE_USER.name()));
        Exception exception = Assertions.assertThrows(RoleNotFoundException.class, () -> {
            authenticationService.registerClient(registerRequest);
        });
        Assertions.assertEquals("Specified Role not found : ROLE_USER", exception.getMessage());
    }

    @Test
    public void testAuthenticateClientSuccess() {
        AuthenticationRequest authenticationRequest = AuthenticationTestSupport.prepareAuthenticationRequest();
        Authentication authentication = AuthenticationTestSupport.prepareAuthentication();
        Mockito.when(authenticationManager.authenticate(Mockito.any())).thenReturn(authentication);
        Mockito.when(jwtService.generateJWT(authentication)).thenReturn("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c");
        AuthenticationResponse authenticationResponse = authenticationService.authenticateClient(authenticationRequest);
        Assertions.assertEquals("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c", authenticationResponse.getJwt());
    }

}
