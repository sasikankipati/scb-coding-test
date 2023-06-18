package com.user.common.advice;

import com.user.common.dto.ErrorResponseDTO;
import com.user.common.exception.UserAlreadyExistsException;
import com.user.exception.UserNotFoundException;
import com.user.security.exception.RoleNotFoundException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;

@RestControllerAdvice
public class ControllerAdvice extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        List<String> fieldErrors = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();
        return new ResponseEntity<>(new ErrorResponseDTO(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({UserAlreadyExistsException.class, UserNotFoundException.class, RoleNotFoundException.class})
    public ResponseEntity<ErrorResponseDTO> iHandleRunTimeException(Exception exception, WebRequest webRequest) {
        return new ResponseEntity<>(new ErrorResponseDTO(exception.getMessage()), HttpStatus.BAD_REQUEST);
    }
}

