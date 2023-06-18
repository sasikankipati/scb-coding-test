package com.user.common;

import com.user.common.constants.RoleEnum;
import com.user.security.core.UserDetailsImpl;
import com.user.security.dto.request.AuthenticationRequest;
import com.user.security.dto.request.RegisterRequest;
import com.user.security.dto.response.AuthenticationResponse;
import com.user.security.dto.response.RegisterResponse;
import com.user.security.entity.AuthUser;
import com.user.security.entity.Role;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface AuthenticationTestSupport {

    static RegisterRequest prepareRegisterRequest() {
        return new RegisterRequest("Username", "Password", List.of(RoleEnum.ROLE_USER));
    }

    static RegisterResponse prepareRegisterResponse() {
        return new RegisterResponse("User registered successfully!");
    }

    static AuthenticationRequest prepareAuthenticationRequest() {
        return new AuthenticationRequest("Username", "Password");
    }

    static AuthenticationResponse prepareAuthenticationResponse() {
        return new AuthenticationResponse("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c",
                "Username", List.of("ROLE_USER"));
    }

    static AuthUser prepareAuthUser() {
        return new AuthUser(1, "UserName", "Password", Set.of(new Role(1, RoleEnum.ROLE_USER)));
    }

    static Authentication prepareAuthentication() {
        return new Authentication() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return List.of(new SimpleGrantedAuthority(RoleEnum.ROLE_USER.name()));
            }

            @Override
            public Object getCredentials() {
                return null;
            }

            @Override
            public Object getDetails() {
                return null;
            }

            @Override
            public Object getPrincipal() {
                return UserDetailsImpl.build(prepareAuthUser());
            }

            @Override
            public boolean isAuthenticated() {
                return true;
            }

            @Override
            public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

            }

            @Override
            public String getName() {
                return null;
            }
        };
    }
}
