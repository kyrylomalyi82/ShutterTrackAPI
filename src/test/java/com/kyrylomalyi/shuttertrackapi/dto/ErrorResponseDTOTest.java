package com.kyrylomalyi.shuttertrackapi.dto;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ErrorResponseDTOTest {

    @Test
    void shouldBuildErrorResponseDTO() {
        ErrorResponseDTO testDto = ErrorResponseDTO.builder()
                .message("message")
                .errorCode("errorCode")
                .path("path")
                .timestamp(LocalDateTime.now())
                .build();

        assertEquals("message", testDto.getMessage());
        assertEquals("errorCode", testDto.getErrorCode());
        assertEquals("path", testDto.getPath());
    }
}