package com.user.common.exception;

public class UserAlreadyExistsException extends RuntimeException{

    public UserAlreadyExistsException(String userNameOrId) {
        super("User already exists with name or id : " + userNameOrId);
    }

    public UserAlreadyExistsException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
