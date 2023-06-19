package com.user.security.service;

import com.user.common.constants.RoleEnum;
import com.user.security.core.ClientDetailsImpl;
import com.user.security.entity.Client;
import com.user.security.entity.Role;
import com.user.security.exception.ClientAlreadyExistsException;
import com.user.security.exception.RoleNotFoundException;
import com.user.security.dto.request.AuthenticationRequest;
import com.user.security.dto.request.RegisterRequest;
import com.user.security.dto.response.AuthenticationResponse;
import com.user.security.dto.response.RegisterResponse;
import com.user.security.repository.ClientRepository;
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

    private final ClientRepository clientRepository;

    private final PasswordEncoder passwordEncoder;

    private final JWTService jwtService;

    private final AuthenticationManager authenticationManager;

    private final RoleRepository roleRepository;

    /**
     * Function to register new Client for Authentication
     * @param registerRequest - Register request with Client details
     * @return - Register Response with registration status
     */
    public RegisterResponse registerClient(RegisterRequest registerRequest) {
        log.info("AuthenticationService > RegisterResponse > Start [clientName : {}]", registerRequest.getClientName());
        if (clientRepository.existsByClientName(registerRequest.getClientName())) {
            log.error("AuthenticationService > RegisterResponse > Client already exists with name : {}]", registerRequest.getClientName());
            throw new ClientAlreadyExistsException(registerRequest.getClientName());
        }

        Client client = new Client(registerRequest.getClientName(), passwordEncoder.encode(registerRequest.getPassword()));
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
        client.setRoles(roles);
        clientRepository.save(client);

        log.info("AuthenticationService > RegisterResponse > End [clientName : {}]", registerRequest.getClientName());
        return new RegisterResponse("Client registered successfully!");
    }

    /**
     * Function to authenticate Client with spring security
     * @param authenticationRequest - Authentication request holds the credentials
     * @return - Authentication Response with generated JWT
     */
    public AuthenticationResponse authenticateClient(AuthenticationRequest authenticationRequest) {
        log.info("AuthenticationService > authenticateClient > Start [clientName : {}]", authenticationRequest.getClientName());
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationRequest.getClientName(), authenticationRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwtToken = jwtService.generateJWT(authentication);

        ClientDetailsImpl clientDetails = (ClientDetailsImpl) authentication.getPrincipal();
        List<String> roles = clientDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        log.info("AuthenticationService > authenticateClient > End [clientName : {}]", authenticationRequest.getClientName());
        return new AuthenticationResponse(jwtToken,
                clientDetails.getUsername(),
                roles);
    }
}