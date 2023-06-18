package com.user.security.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "AUTH_USERS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthUser implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue()
    private Integer id;

    @Column(nullable = false, updatable = true)
    private String userName;

    @Column(nullable = false, updatable = true)
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "AUTH_USER_ROLES",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    public AuthUser(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

}
