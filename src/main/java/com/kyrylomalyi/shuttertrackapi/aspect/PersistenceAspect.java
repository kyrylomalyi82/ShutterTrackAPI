package com.kyrylomalyi.shuttertrackapi.aspect;

import com.kyrylomalyi.shuttertrackapi.dto.ShutterResponseDTO;
import com.kyrylomalyi.shuttertrackapi.entity.ShutterResultEntity;
import com.kyrylomalyi.shuttertrackapi.repository.ShutterResultRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class PersistenceAspect {

    private final ShutterResultRepository shutterResultRepository;

    @AfterReturning(
            pointcut = "execution(* com.kyrylomalyi.shuttertrackapi.service.ShutterService.extractShutterCount(..))",
            returning = "responseDto"
    )
    public void saveResponse(ShutterResponseDTO responseDto) {
        try {
            log.debug("Saving shutter result for file: {}", responseDto.getFileName());

            ShutterResultEntity entityToSave = ShutterResultEntity.builder()
                    .fileName(responseDto.getFileName())
                    .shutterCount(responseDto.getShutterCount())
                    .cameraModel(responseDto.getCameraModel())
                    .createdAt(LocalDateTime.now())
                    .build();

            shutterResultRepository.save(entityToSave);

            log.info("Successfully saved shutter result for file: {} with count: {}",
                    responseDto.getFileName(), responseDto.getShutterCount());

        } catch (Exception e) {
            log.error("Failed to save shutter result for file: {}", responseDto.getFileName(), e);
        }
    }
}