package com.kyrylomalyi.shuttertrackapi.web;

import com.kyrylomalyi.shuttertrackapi.dto.AnalyticsResponseDTO;
import com.kyrylomalyi.shuttertrackapi.service.AnalyticService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AnalyticsController {

    private final AnalyticService analyticService;

    public AnalyticsController(AnalyticService analyticService) {
        this.analyticService = analyticService;
    }

    @GetMapping("/analytics")
    public ResponseEntity<AnalyticsResponseDTO> getAnalytics() {
        return ResponseEntity.ok(analyticService.getAnalysis());
    }

}
