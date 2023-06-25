package com.user.security.rest;

import com.user.common.dto.ErrorResponseDTO;
import com.user.common.metadata.RestControllerWithPathMapping;
import com.user.security.dto.request.AuthenticationRequest;
import com.user.security.dto.request.RegisterRequest;
import com.user.security.dto.response.AuthenticationResponse;
import com.user.security.dto.response.RegisterResponse;
import com.user.security.service.AuthenticationService;
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

    @Operation(summary = "Register Client for authentication")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Client Registered successfully", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = RegisterResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Client with name already exists", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))})
    })
    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> doRegistration(@Validated @RequestBody @Parameter(description = "Register Client request") RegisterRequest registerRequest) {
        log.info("AuthenticationController > doRegistration > Start [clientName : {}]", registerRequest.getClientName());
        RegisterResponse registerResponse = authenticationService.registerClient(registerRequest);
        log.info("AuthenticationController > doRegistration > End");
        return new ResponseEntity<>(registerResponse, HttpStatus.CREATED);
    }

    @Operation(summary = "Authenticate Client to generate JWT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Client Authenticated successfully", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = AuthenticationResponse.class))}),
            @ApiResponse(responseCode = "401", description = "Client not available (or) Password not matched", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))})
    })
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> doAuthentication(@Validated @RequestBody @Parameter(description = "Authenticate Client request") AuthenticationRequest authenticationRequest) {
        log.info("AuthenticationController > doAuthentication > Start [clientName : {}]", authenticationRequest.getClientName());
        AuthenticationResponse authenticationResponse = authenticationService.authenticateClient(authenticationRequest);
        log.info("AuthenticationController > doAuthentication > End");
        return ResponseEntity.ok(authenticationResponse);
    }
}