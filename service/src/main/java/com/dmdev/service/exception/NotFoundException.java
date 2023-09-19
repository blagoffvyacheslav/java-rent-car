package com.dmdev.service.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;


public class NotFoundException extends ResponseStatusException {

    public static String prepare(String entity, String attr, Object val){
        return entity + " with " + attr + " " + val + " does not exist.";
    }

    public NotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}