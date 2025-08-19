package com.kyrylomalyi.shuttertrackapi.aspect;

import com.kyrylomalyi.shuttertrackapi.util.ValidationUtil;
import com.kyrylomalyi.shuttertrackapi.web.ShutterController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@EnableAspectJAutoProxy
@WebMvcTest(ShutterController.class)
@Import({ValidateAspect.class})
class ValidateAspectTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ValidationUtil validationUtil;


    @Test
    void givenValidFile_whenTargetMethodIsCalled_thenValidatorIsCalledAndExecutionProceeds() throws Exception {
        MockMultipartFile validFile = new MockMultipartFile(
                "file",
                "image.nef",
                "image/x-nikon-nef",
                "test content".getBytes()
        );

        doNothing().when(validationUtil).validateFile(any());

        mockMvc.perform(multipart("/api/raw/upload")
                        .file(validFile))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fileName").value("image.nef"));

        verify(validationUtil, times(1)).validateFile(any());
    }



}