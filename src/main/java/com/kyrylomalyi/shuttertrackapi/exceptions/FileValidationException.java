package com.kyrylomalyi.shuttertrackapi.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class FileValidationException extends RuntimeException {
    public FileValidationException(String message) {
        super(message);
    }
}
