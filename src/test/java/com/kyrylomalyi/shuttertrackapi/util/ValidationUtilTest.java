package com.kyrylomalyi.shuttertrackapi.util;

import com.kyrylomalyi.shuttertrackapi.exceptions.FileValidationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class ValidationUtilTest {

    @Autowired
    private ValidationUtil validationUtil;


    @Test
    void givenValidFile_whenValidateFile_thenNoExceptionIsThrown(){
        MultipartFile validFile = new MockMultipartFile(
                "file",
                "IMG_1234.CR2",
                "image/x-canon-cr2",
                new byte[157286400]);

        assertDoesNotThrow(() -> validationUtil.validateFile(validFile));
    }

    @Test
    void givenUnsupportedFile_whenValidateFile_thenThrowsValidationException(){
        MultipartFile invalidFile = new MockMultipartFile(
                "file" ,
                "document.txt" ,
                "text/plain" ,
                new byte[1024 * 1024]
        );

        FileValidationException exception = assertThrows(FileValidationException.class, () -> validationUtil.validateFile(invalidFile));
        assertEquals("The file is not supported", exception.getMessage());
    }

    @Test
    void givenOversizedFile_whenValidateFile_thenThrowsValidationException(){
        MultipartFile invalidFile = new MockMultipartFile(
                "file",
                "big_photo.arw",
                "image/x-sony-arw",
                new byte[157286401]);

        FileValidationException exception = assertThrows(FileValidationException.class, () -> validationUtil.validateFile(invalidFile));

        assertEquals("The file is too large", exception.getMessage());
    }



}