package com.user.security.core;

import com.user.security.entity.Client;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ClientDetailsImpl implements UserDetails {

    private final Integer id;

    private final String clientName;

    private final String password;

    private final Collection<? extends GrantedAuthority> authorities;

    public ClientDetailsImpl(
            Integer id, String clientName, String password,
            Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.clientName = clientName;
        this.password = password;
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public Integer id() {
        return id;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return clientName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public static ClientDetailsImpl build(Client client) {
        List<GrantedAuthority> authorities = client.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());

        return new ClientDetailsImpl(
                client.getId(),
                client.getClientName(),
                client.getPassword(),
                authorities
        );
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        } else if (other == null || getClass() != other.getClass()) {
            return false;
        } else {
            ClientDetailsImpl clientDetails = (ClientDetailsImpl) other;
            return Objects.equals(id, clientDetails.id);
        }
    }
}
