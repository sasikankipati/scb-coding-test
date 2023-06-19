package com.user.security.rest;

import com.user.common.AuthenticationTestSupport;
import com.user.security.dto.request.AuthenticationRequest;
import com.user.security.dto.request.RegisterRequest;
import com.user.security.dto.response.AuthenticationResponse;
import com.user.security.dto.response.RegisterResponse;
import com.user.security.exception.ClientAlreadyExistsException;
import com.user.security.service.AuthenticationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AuthenticationControllerTest {

    @InjectMocks
    AuthenticationController authenticationController;

    @Mock
    AuthenticationService authenticationService;

    @Test
    public void testDoRegistrationSuccess() {
        RegisterRequest registerRequest = AuthenticationTestSupport.prepareRegisterRequest();
        Mockito.when(authenticationService.registerClient(registerRequest)).thenReturn(AuthenticationTestSupport.prepareRegisterResponse());
        RegisterResponse registerResponse = authenticationController.doRegistration(registerRequest).getBody();
        Assertions.assertEquals("Client registered successfully!", registerResponse.getMessage());
    }

    @Test
    public void testDoRegistrationFailure() {
        RegisterRequest registerRequest = AuthenticationTestSupport.prepareRegisterRequest();
        Mockito.when(authenticationService.registerClient(registerRequest)).thenThrow(new ClientAlreadyExistsException("Username"));
        Exception exception = Assertions.assertThrows(ClientAlreadyExistsException.class, () -> {
            authenticationController.doRegistration(registerRequest);
        });
        Assertions.assertEquals("Client already exists with name : Username", exception.getMessage());
    }

    @Test
    public void testDoAuthenticationSuccess() {
        AuthenticationRequest authenticationRequest = AuthenticationTestSupport.prepareAuthenticationRequest();
        Mockito.when(authenticationService.authenticateClient(authenticationRequest)).thenReturn(AuthenticationTestSupport.prepareAuthenticationResponse());
        AuthenticationResponse authenticationResponse = authenticationController.doAuthentication(authenticationRequest).getBody();
        Assertions.assertEquals("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c", authenticationResponse.getJwt());
    }

    @Test
    public void testDoAuthenticationFailure() {
        AuthenticationRequest authenticationRequest = AuthenticationTestSupport.prepareAuthenticationRequest();
        Mockito.when(authenticationService.authenticateClient(authenticationRequest)).thenThrow(new RuntimeException("UnAuthorized"));
        Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
            authenticationController.doAuthentication(authenticationRequest);
        });
        Assertions.assertEquals("UnAuthorized", exception.getMessage());
    }
}