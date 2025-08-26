package com.kyrylomalyi.shuttertrackapi.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AnalyticsResponseDTO {
   private Long totalUploads;
   private Long averageShutterCount;
   private String mostFrequentCamera;
}
