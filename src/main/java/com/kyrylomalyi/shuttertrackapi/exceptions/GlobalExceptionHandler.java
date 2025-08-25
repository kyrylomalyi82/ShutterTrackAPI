package com.kyrylomalyi.shuttertrackapi.exceptions;

import com.kyrylomalyi.shuttertrackapi.dto.ErrorResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(FileValidationException.class)
    public ResponseEntity<ErrorResponseDTO> handleFileValidationException(FileValidationException ex) {
        ErrorResponseDTO error = ErrorResponseDTO.builder()
                .message(ex.getMessage())
                .errorCode("FILE_VALIDATION_ERROR")
                .path("/api/raw/upload")
                .timestamp(LocalDateTime.now()).build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(MetadataExtractionException.class)
    public ResponseEntity<ErrorResponseDTO> handleMetadataExtractionException(MetadataExtractionException ex) {
        ErrorResponseDTO error = ErrorResponseDTO.builder()
                .message(ex.getMessage())
                .errorCode("METADATA_EXTRACTION_ERROR")
                .path("/api/raw/upload")
                .timestamp(LocalDateTime.now()).build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGenericException(Exception ex) {
        ErrorResponseDTO error = ErrorResponseDTO.builder()
                .message(ex.getMessage())
                .errorCode("GENERIC_ERROR")
                .path("/api/raw/upload")
                .timestamp(LocalDateTime.now()).build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }




}
