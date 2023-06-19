package com.user.exception;

public class UserAlreadyExistsException extends RuntimeException{

    public UserAlreadyExistsException(String userId) {
        super("User already exists with id : " + userId);
    }

}
