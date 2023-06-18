package com.user.security.entity;

import com.user.common.constants.RoleEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ROLES")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    @Id
    @GeneratedValue()
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column
    private RoleEnum name;

}
