package com.kyrylomalyi.shuttertrackapi.service;

import com.kyrylomalyi.shuttertrackapi.dto.AnalyticsResponseDTO;
import com.kyrylomalyi.shuttertrackapi.entity.ShutterResultEntity;
import com.kyrylomalyi.shuttertrackapi.repository.ShutterResultRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AnalyticServiceTest {

    @Autowired
    private ShutterResultRepository shutterResultRepository;

    @Autowired
    private AnalyticService analyticService;

    @Test
    void shouldReturnAnalyticsResponseDTO() {
       AnalyticsResponseDTO responseDTO = analyticService.getAnalysis();

       assertNotNull(responseDTO);
       assertNotNull(responseDTO.getMostFrequentCamera());
       assertNotNull(responseDTO.getAverageShutterCount());
       assertNotNull(responseDTO.getTotalUploads());

    }

    @Test
    void shouldReturnCorrectAnalyticsWithData() {
        // given
        shutterResultRepository.save(ShutterResultEntity.builder()
                .fileName("file1.jpg")
                .shutterCount(100)
                .cameraModel("Canon R8")
                .build());

        shutterResultRepository.save(ShutterResultEntity.builder()
                .fileName("file2.jpg")
                .shutterCount(200)
                .cameraModel("Canon R8")
                .build());

        shutterResultRepository.save(ShutterResultEntity.builder()
                .fileName("file3.jpg")
                .shutterCount(300)
                .cameraModel("Nikon Z6")
                .build());

        // when
        AnalyticsResponseDTO response = analyticService.getAnalysis();

        // then
        assertNotNull(response);
        assertEquals(3L, response.getTotalUploads());
        assertEquals(200L, response.getAverageShutterCount());
        assertEquals("Canon R8", response.getMostFrequentCamera());
    }

}