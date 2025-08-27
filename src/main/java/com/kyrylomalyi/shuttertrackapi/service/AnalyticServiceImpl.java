package com.kyrylomalyi.shuttertrackapi.service;

import com.kyrylomalyi.shuttertrackapi.dto.AnalyticsResponseDTO;
import com.kyrylomalyi.shuttertrackapi.repository.ShutterResultRepository;
import org.springframework.stereotype.Service;

@Service
public class AnalyticServiceImpl implements AnalyticService{

    private final ShutterResultRepository shutterResultRepository;

    public AnalyticServiceImpl(ShutterResultRepository shutterResultRepository) {
        this.shutterResultRepository = shutterResultRepository;
    }

    @Override
    public AnalyticsResponseDTO getAnalysis() {
        return AnalyticsResponseDTO
                .builder().
                totalUploads(getTotalUploads())
                .averageShutterCount(getAverageShutterCount())
                .mostFrequentCamera(getMostFrequentCamera())
                .build();
    }

    public Long getTotalUploads() {
        return shutterResultRepository.count();
    }

    public Long getAverageShutterCount() {
        return shutterResultRepository.findAverageShutterCount();
    }

    public String getMostFrequentCamera() {
        return shutterResultRepository.findMostFrequentCamera();
    }
}
