package com.user.security.exception;

public class ClientAlreadyExistsException extends RuntimeException{

    public ClientAlreadyExistsException(String clientName) {
        super("Client already exists with name : " + clientName);
    }

    public ClientAlreadyExistsException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
