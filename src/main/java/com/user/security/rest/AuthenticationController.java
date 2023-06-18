package com.user.security.rest;

import com.user.common.dto.ErrorResponseDTO;
import com.user.security.dto.request.AuthenticationRequest;
import com.user.security.dto.request.RegisterRequest;
import com.user.security.dto.response.AuthenticationResponse;
import com.user.security.dto.response.RegisterResponse;
import com.user.security.service.AuthenticationService;
import com.user.common.metadata.RestControllerWithPathMapping;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestControllerWithPathMapping(requestPath = "/auth")
@Slf4j
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @Operation(summary = "Register User for authentication")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User Registered successfully", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = RegisterResponse.class))}),
            @ApiResponse(responseCode = "400", description = "User already exists", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))})
            })
    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> doRegistration(@Validated @RequestBody @Parameter(description = "Register User request") RegisterRequest registerRequest) {
        log.info("AuthenticationController > doRegistration > Start [userName : {}]", registerRequest.getUserName());
        RegisterResponse registerResponse = authenticationService.registerUser(registerRequest);
        log.info("AuthenticationController > doRegistration > End");
        return new ResponseEntity<>(registerResponse, HttpStatus.CREATED);
    }

    @Operation(summary = "Authenticate User to generate JWT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User Authenticated successfully", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = AuthenticationResponse.class))}),
            @ApiResponse(responseCode = "401", description = "User not available (or) Password not matched", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))})
    })
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> doAuthentication(@Validated @RequestBody @Parameter(description = "Authenticate User request") AuthenticationRequest authenticationRequest) {
        log.info("AuthenticationController > doAuthentication > Start [userName : {}]", authenticationRequest.getUserName());
        AuthenticationResponse authenticationResponse = authenticationService.authenticateUser(authenticationRequest);
        log.info("AuthenticationController > doAuthentication > End");
        return ResponseEntity.ok(authenticationResponse);
    }
}