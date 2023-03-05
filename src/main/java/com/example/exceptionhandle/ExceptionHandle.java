package com.example.exceptionhandle;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.text.ParseException;

@RestControllerAdvice
public class ExceptionHandle extends ResponseEntityExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(UserNotEnable.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleAllException(ParseException ex, WebRequest request) {
        UserNotEnable usernameNotFoundException = new UserNotEnable(ex.getMessage());
        return new ResponseEntity<>(usernameNotFoundException, HttpStatus.NOT_FOUND);

    }



}
