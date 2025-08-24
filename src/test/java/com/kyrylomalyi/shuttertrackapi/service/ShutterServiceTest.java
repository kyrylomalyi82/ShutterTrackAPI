package com.kyrylomalyi.shuttertrackapi.service;

import com.kyrylomalyi.shuttertrackapi.dto.ShutterResponseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class ShutterServiceTest {

    final String originalFileName = "_DSC4175.NEF";

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
        assertEquals(1090, result.getShutterCount());
    }

    @Test
    void shouldExtractShutterCountFromCanonCR2() throws Exception {
        // Arrange
        final String canonFileName = "cr2-sample-file.cr2";
        final String expectedMimeType = "image/x-canon-cr2";

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(canonFileName);
        assertNotNull(inputStream, "Test file not found: " + canonFileName);

        MultipartFile testCR2 = new MockMultipartFile("file", canonFileName, expectedMimeType, inputStream);

        // Act
        ShutterResponseDTO result = shutterService.extractShutterCount(testCR2);

        // Assert
        assertNotNull(result, "Result should not be null");
        assertNotNull(result.getShutterCount(), "Shutter count should not be null");
        assertEquals(6 , result.getShutterCount());
        assertEquals(canonFileName, result.getFileName(), "File name should match");
    }

}