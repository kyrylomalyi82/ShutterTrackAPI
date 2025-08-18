package com.kyrylomalyi.shuttertrackapi.dto;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ShutterResponseDTOTest {

    @Test
    void shouldBuildShutterResponseDTO() {
        ShutterResponseDTO testDto = ShutterResponseDTO.builder()
                .fileName("IMG_1234.CR2")
                .cameraModel("Canon EOS R5")
                .shutterCount(5000)
                .build();

        assertEquals("IMG_1234.CR2", testDto.getFileName());
        assertEquals("Canon EOS R5", testDto.getCameraModel());
        assertEquals(5000, testDto.getShutterCount());
    }
}