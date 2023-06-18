package com.user.security.service;

import com.user.common.AuthenticationTestSupport;
import com.user.common.constants.RoleEnum;
import com.user.common.exception.UserAlreadyExistsException;
import com.user.security.dto.request.AuthenticationRequest;
import com.user.security.dto.request.RegisterRequest;
import com.user.security.dto.response.AuthenticationResponse;
import com.user.security.dto.response.RegisterResponse;
import com.user.security.entity.AuthUser;
import com.user.security.entity.Role;
import com.user.security.exception.RoleNotFoundException;
import com.user.security.repository.AuthUserRepository;
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
    AuthUserRepository authUserRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    JWTService jwtService;

    @Mock
    AuthenticationManager authenticationManager;

    @Mock
    RoleRepository roleRepository;

    @Test
    public void testRegisterUserSuccess() {
        RegisterRequest registerRequest = AuthenticationTestSupport.prepareRegisterRequest();
        Mockito.when(authUserRepository.existsByUserName(registerRequest.getUserName())).thenReturn(false);
        Mockito.when(passwordEncoder.encode(registerRequest.getPassword())).thenReturn("wtertewrewurewur");
        Mockito.when(roleRepository.findByName(RoleEnum.ROLE_USER)).thenReturn(Optional.of(new Role(1, RoleEnum.ROLE_USER)));
        Mockito.when(authUserRepository.save(Mockito.any())).thenReturn(new AuthUser());
        RegisterResponse registerResponse = authenticationService.registerUser(registerRequest);
        Assertions.assertEquals("User registered successfully!", registerResponse.getMessage());
    }

    @Test
    public void testRegisterUserSuccessWithoutRoles() {
        RegisterRequest registerRequest = AuthenticationTestSupport.prepareRegisterRequest();
        registerRequest.setRoles(null);
        Mockito.when(authUserRepository.existsByUserName(registerRequest.getUserName())).thenReturn(false);
        Mockito.when(passwordEncoder.encode(registerRequest.getPassword())).thenReturn("wtertewrewurewur");
        Mockito.when(roleRepository.findByName(RoleEnum.ROLE_USER)).thenReturn(Optional.of(new Role(1, RoleEnum.ROLE_USER)));
        Mockito.when(authUserRepository.save(Mockito.any())).thenReturn(new AuthUser());
        RegisterResponse registerResponse = authenticationService.registerUser(registerRequest);
        Assertions.assertEquals("User registered successfully!", registerResponse.getMessage());
    }

    @Test
    public void testRegisterUserFailureOnDuplicateUser() {
        RegisterRequest registerRequest = AuthenticationTestSupport.prepareRegisterRequest();
        Mockito.when(authUserRepository.existsByUserName(registerRequest.getUserName())).thenReturn(true);
        Exception exception = Assertions.assertThrows(UserAlreadyExistsException.class, () -> {
            authenticationService.registerUser(registerRequest);
        });
        Assertions.assertEquals("User already exists with name or id : Username", exception.getMessage());
    }

    @Test
    public void testRegisterUserFailureOnRoleNotFound() {
        RegisterRequest registerRequest = AuthenticationTestSupport.prepareRegisterRequest();
        Mockito.when(authUserRepository.existsByUserName(registerRequest.getUserName())).thenReturn(false);
        Mockito.when(passwordEncoder.encode(registerRequest.getPassword())).thenReturn("wtertewrewurewur");
        Mockito.when(roleRepository.findByName(RoleEnum.ROLE_USER)).thenThrow(new RoleNotFoundException(RoleEnum.ROLE_USER.name()));
        Exception exception = Assertions.assertThrows(RoleNotFoundException.class, () -> {
            authenticationService.registerUser(registerRequest);
        });
        Assertions.assertEquals("Specified Role not found : ROLE_USER", exception.getMessage());
    }

    @Test
    public void testAuthenticateUserSuccess() {
        AuthenticationRequest authenticationRequest = AuthenticationTestSupport.prepareAuthenticationRequest();
        Authentication authentication = AuthenticationTestSupport.prepareAuthentication();
        Mockito.when(authenticationManager.authenticate(Mockito.any())).thenReturn(authentication);
        Mockito.when(jwtService.generateJWT(authentication)).thenReturn("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c");
        AuthenticationResponse authenticationResponse = authenticationService.authenticateUser(authenticationRequest);
        Assertions.assertEquals("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c", authenticationResponse.getJwt());
    }

}
