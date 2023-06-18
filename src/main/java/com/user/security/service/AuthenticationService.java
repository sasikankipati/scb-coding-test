package com.user.security.service;

import com.user.common.constants.RoleEnum;
import com.user.security.core.UserDetailsImpl;
import com.user.security.entity.AuthUser;
import com.user.security.entity.Role;
import com.user.common.exception.UserAlreadyExistsException;
import com.user.security.exception.RoleNotFoundException;
import com.user.security.dto.request.AuthenticationRequest;
import com.user.security.dto.request.RegisterRequest;
import com.user.security.dto.response.AuthenticationResponse;
import com.user.security.dto.response.RegisterResponse;
import com.user.security.repository.AuthUserRepository;
import com.user.security.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthUserRepository authUserRepository;

    private final PasswordEncoder passwordEncoder;

    private final JWTService jwtService;

    private final AuthenticationManager authenticationManager;

    private final RoleRepository roleRepository;

    /**
     * Function to register new user for Authentication
     * @param registerRequest - Register request with user details
     * @return - Register Response with registration status
     */
    public RegisterResponse registerUser(RegisterRequest registerRequest) {
        log.info("AuthenticationService > RegisterResponse > Start [userName : {}]", registerRequest.getUserName());
        if (authUserRepository.existsByUserName(registerRequest.getUserName())) {
            log.error("AuthenticationService > RegisterResponse > User already exists with name : {}]", registerRequest.getUserName());
            throw new UserAlreadyExistsException(registerRequest.getUserName());
        }

        AuthUser authUser = new AuthUser(registerRequest.getUserName(), passwordEncoder.encode(registerRequest.getPassword()));
        Set<Role> roles = new HashSet<>();

        if (CollectionUtils.isEmpty(registerRequest.getRoles())) {
            Role role = roleRepository.findByName(RoleEnum.ROLE_USER)
                    .orElseThrow(() -> new RoleNotFoundException(RoleEnum.ROLE_USER.name()));
            roles.add(role);
        } else {
            registerRequest.getRoles().forEach(inputRole -> {
                Role role = roleRepository.findByName(inputRole)
                        .orElseThrow(() -> new RoleNotFoundException(inputRole.name()));
                roles.add(role);
            });
        }
        authUser.setRoles(roles);
        authUserRepository.save(authUser);

        log.info("AuthenticationService > RegisterResponse > End [userName : {}]", registerRequest.getUserName());
        return new RegisterResponse("User registered successfully!");
    }

    /**
     * Function to authenticate user with spring security
     * @param authenticationRequest - Authentication request holds the credentials
     * @return - Authentication Response with generated JWT
     */
    public AuthenticationResponse authenticateUser(AuthenticationRequest authenticationRequest) {
        log.info("AuthenticationService > authenticateUser > Start [userName : {}]", authenticationRequest.getUserName());
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationRequest.getUserName(), authenticationRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwtToken = jwtService.generateJWT(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        log.info("AuthenticationService > authenticateUser > End [userName : {}]", authenticationRequest.getUserName());
        return new AuthenticationResponse(jwtToken,
                userDetails.getUsername(),
                roles);
    }
}