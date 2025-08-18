package com.kyrylomalyi.shuttertrackapi.web;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
@WebMvcTest(ShutterController.class)
class ShutterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void whenUploadFile_shouldReturn200() throws Exception {
        MockMultipartFile mockFile = new MockMultipartFile(
                "file", // должно совпадать с @RequestParam("file")
                "IMG_1234.CR2", // имя файла
                "image/x-canon-cr2", // content type для CR2
                "fake-raw-content".getBytes() // фиктивное содержимое
        );

        mockMvc.perform(multipart("/api/raw/upload").file(mockFile))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.cameraModel").value("Canon EOS 5D Mark IV"))
                .andExpect(jsonPath("$.fileName").value("IMG_1234.CR2"))
                .andExpect(jsonPath("$.shutterCount").value(12500));

    }
}