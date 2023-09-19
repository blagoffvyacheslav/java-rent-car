package com.dmdev.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class BadRequestException extends ResponseStatusException {

    public static String existsPrepare(String entity, String attr, Object val){
        return entity + " with " + attr + " " + val + " already exists";
    }

    public BadRequestException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}