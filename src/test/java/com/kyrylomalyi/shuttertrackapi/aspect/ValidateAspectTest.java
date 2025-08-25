package com.kyrylomalyi.shuttertrackapi.aspect;

import com.kyrylomalyi.shuttertrackapi.util.ValidationUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.io.InputStream;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ValidateAspectTest {

    private final String originalFileName = "_DSC4175.NEF";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ValidationUtil validationUtil;

    @Test
    void aspectIsCalledBeforeController() throws Exception {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(originalFileName);
        MockMultipartFile testRaw = new MockMultipartFile(
                "file",
                originalFileName,
                "image/x-nikon-nef",
                inputStream
        );
        doNothing().when(validationUtil).validateFile(any());

        mockMvc.perform(multipart("/api/raw/upload").file(testRaw))
                .andExpect(status().isOk());

        verify(validationUtil, times(1)).validateFile(any());
    }
}