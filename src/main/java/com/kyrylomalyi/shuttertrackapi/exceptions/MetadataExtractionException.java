package com.kyrylomalyi.shuttertrackapi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MetadataExtractionException extends RuntimeException {
    public MetadataExtractionException(String message) {
        super(message);
    }
}
