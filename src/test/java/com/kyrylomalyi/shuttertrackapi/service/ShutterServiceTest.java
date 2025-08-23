package com.kyrylomalyi.shuttertrackapi.service;

import com.kyrylomalyi.shuttertrackapi.dto.ShutterResponseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest
class ShutterServiceTest {

    final String originalFileName = "_DSC4175.NEF";
    final int EXPECTED_SHUTTER_COUNT = 1090;

    @Autowired
    private ShutterService shutterService;

    @Test
    void shouldExtractCameraModel() throws Exception {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(originalFileName);
        assertNotNull(inputStream, "Test file is not founded " + originalFileName);

        MultipartFile testRaw = new MockMultipartFile("file", originalFileName, "image/x-nikon-nef", inputStream);

        // Act
        ShutterResponseDTO result = shutterService.extractShutterCount(testRaw);

        // asserts
        assertNotNull(result);
        assertEquals("NIKON D700" , result.getCameraModel() );
    }

    @Test
    void shouldReturnFileName () throws Exception {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(originalFileName);
        assertNotNull(inputStream, "Test file is not founded " + originalFileName);

        MultipartFile testRaw = new MockMultipartFile("file", originalFileName, "image/x-nikon-nef", inputStream);

        ShutterResponseDTO result = shutterService.extractShutterCount(testRaw);

        assertNotNull(result);
        assertEquals("_DSC4175.NEF" , result.getFileName() );
    }

    @Test
    void shouldReturnShutterCount () throws Exception {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(originalFileName);
        assertNotNull(inputStream, "Test file is not founded " + originalFileName);

        MultipartFile testRaw = new MockMultipartFile("file", originalFileName, "image/x-nikon-nef", inputStream);

        ShutterResponseDTO result = shutterService.extractShutterCount(testRaw);
        System.out.println(result);

        assertNotNull(result);
        assertEquals(EXPECTED_SHUTTER_COUNT, result.getShutterCount());
    }

}