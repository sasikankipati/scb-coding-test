package com.user.security.exception;

public class RoleNotFoundException extends RuntimeException {

    public RoleNotFoundException(String roleName) {
        super("Specified Role not found : " + roleName);
    }
}
