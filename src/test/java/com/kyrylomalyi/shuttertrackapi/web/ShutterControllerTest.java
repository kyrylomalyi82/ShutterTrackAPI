package com.kyrylomalyi.shuttertrackapi.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.io.InputStream;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ShutterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final String originalFileName = "_DSC4175.NEF";


    @Test
    void whenUploadFile_shouldReturnShutterInfo() throws Exception {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(originalFileName);
        assertNotNull(inputStream, "Test file not found: " + originalFileName);

        MockMultipartFile testRaw = new MockMultipartFile(
                "file",
                originalFileName,
                "image/x-nikon-nef",
                inputStream
        );

        mockMvc.perform(multipart("/api/raw/upload").file(testRaw))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.fileName").value(originalFileName))
                .andExpect(jsonPath("$.cameraModel").isNotEmpty())
                .andExpect(jsonPath("$.shutterCount").isNumber());
    }
}