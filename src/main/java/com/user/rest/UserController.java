package com.user.rest;

import com.user.common.constants.AppConstants;
import com.user.common.dto.ErrorResponseDTO;
import com.user.common.metadata.RestControllerWithPathMapping;
import com.user.security.dto.response.RegisterResponse;
import com.user.service.UserService;
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
import org.springframework.web.bind.annotation.*;

@RestControllerWithPathMapping(requestPath = "/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "Get User By Id or Get All Users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User(s) retrieved successfully", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = RegisterResponse.class))}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))})
    })

    @GetMapping
    public ResponseEntity<?> getUserByIdOrAll(@Parameter(name = "Authorization", required = true) @RequestHeader(AppConstants.AUTHORIZATION) final String authorizationHeader,
                                              @Parameter(required = true, name = "User Id", description = "Id related to User") @RequestParam(required = false) Integer userId) {
        log.info("UserController > getUserByIdOrAll > Start [userId : {}]", userId);
        return ResponseEntity.ok(userService.getUserByIdOrAll(userId));
    }

    @Operation(summary = "Save User details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User saved successfully", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "400", description = "Bad request", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))})
    })
    
    @PostMapping
    public ResponseEntity<String> saveUser(@Parameter(name = "Authorization", required = true) @RequestHeader(AppConstants.AUTHORIZATION) final String authorizationHeader,
                                           @Parameter(name = "User Id", description = "Id related to User") @RequestParam Integer userId,
                                           @Parameter(name = "User Name", description = "Name of the User") @RequestParam String name,
                                           @Parameter(name = "User City", description = "City of the User")@RequestParam String city) {
        log.info("UserController > saveUser > Start [userId : {}, name : {}, city : {}]", userId, name, city);
        userService.saveUser(userId, name, city);
        log.info("UserController > saveUser > End [userId : {}]", userId);
        return new ResponseEntity<>(AppConstants.SUCCESS, HttpStatus.CREATED);
    }

    @Operation(summary = "Update User details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "400", description = "Bad request", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))})
    })
    @PutMapping
    public ResponseEntity<String> updateUser(@Parameter(name = "Authorization", required = true) @RequestHeader(AppConstants.AUTHORIZATION) final String authorizationHeader,
                                             @Parameter(name = "User Id", description = "Id related to User") @RequestParam Integer userId,
                                             @Parameter(name = "User Name", description = "New Name of the User") @RequestParam(required = false) String name,
                                             @Parameter(name = "User City", description = "New City of the User") @RequestParam(required = false) String city) {
        log.info("UserController > updateUser > Start [userId : {}, name : {}, city : {}]", userId, name, city);
        userService.updateUserById(userId, name, city);
        log.info("UserController > updateUser > End [userId : {}]", userId);
        return ResponseEntity.ok(AppConstants.SUCCESS);
    }

    @Operation(summary = "Delete User details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deleted successfully", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "400", description = "Bad request", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))})
    })
    @DeleteMapping
    public ResponseEntity<String> deleteUser(@Parameter(name = "Authorization", required = true) @RequestHeader(AppConstants.AUTHORIZATION) final String authorizationHeader,
                                             @Parameter(name = "User Id", description = "Id related to User") @RequestParam Integer userId) {
        log.info("UserController > deleteUser > Start [userId : {}]", userId);
        userService.deleteUserById(userId);
        log.info("UserController > deleteUser > End [userId : {}]", userId);
        return ResponseEntity.ok(AppConstants.SUCCESS);
    }
}