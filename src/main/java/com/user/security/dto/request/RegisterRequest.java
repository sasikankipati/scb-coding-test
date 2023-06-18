package com.user.security.dto.request;

import com.user.common.constants.RoleEnum;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    @NotNull(message = "UserName is required")
    private String userName;
    @NotNull(message = "Password is required")
    private String password;
    private List<RoleEnum> roles;

}
