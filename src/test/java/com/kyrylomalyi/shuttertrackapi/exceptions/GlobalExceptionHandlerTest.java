package com.kyrylomalyi.shuttertrackapi.exceptions;

import com.kyrylomalyi.shuttertrackapi.dto.ErrorResponseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GlobalExceptionHandlerTest {

    @Autowired
    private GlobalExceptionHandler globalExceptionHandler;

    @Test
    void shouldHandleFileValidationException() {
        FileValidationException ex = new FileValidationException("Invalid file");

        ResponseEntity<ErrorResponseDTO> response = globalExceptionHandler.handleFileValidationException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Invalid file", response.getBody().getMessage());
        assertEquals("FILE_VALIDATION_ERROR", response.getBody().getErrorCode());
    }

    @Test
    void shouldHandleMetaExtractionException() {
        MetadataExtractionException ex = new MetadataExtractionException("Metadata extraction error");

        ResponseEntity<ErrorResponseDTO> response = globalExceptionHandler.handleMetadataExtractionException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Metadata extraction error", response.getBody().getMessage());
        assertEquals("METADATA_EXTRACTION_ERROR", response.getBody().getErrorCode());
    }

    @Test
    void shouldHandleGenericException() {
        RuntimeException ex = new RuntimeException("Unexpected error");

        ResponseEntity<ErrorResponseDTO> response = globalExceptionHandler.handleGenericException(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Unexpected error", response.getBody().getMessage());
        assertEquals("GENERIC_ERROR", response.getBody().getErrorCode());
    }


}