package com.kyrylomalyi.shuttertrackapi.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ShutterResponseDTO {
    private String fileName;
    private String cameraModel;
    private Integer shutterCount;
}


