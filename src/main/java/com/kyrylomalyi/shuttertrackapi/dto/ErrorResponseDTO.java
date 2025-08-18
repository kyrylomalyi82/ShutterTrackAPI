package com.kyrylomalyi.shuttertrackapi.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ErrorResponseDTO {
    private String message;
    private String errorCode;
    private String path;
    private LocalDateTime timestamp;
}
