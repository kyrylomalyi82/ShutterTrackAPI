package com.kyrylomalyi.shuttertrackapi.aspect;

import com.kyrylomalyi.shuttertrackapi.dto.ShutterResponseDTO;
import com.kyrylomalyi.shuttertrackapi.entity.ShutterResultEntity;
import com.kyrylomalyi.shuttertrackapi.repository.ShutterResultRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersistenceAspectTest {

    @Mock
    private ShutterResultRepository shutterResultRepository;

    private PersistenceAspect persistenceAspect;

    @BeforeEach
    void setUp() {
        persistenceAspect = new PersistenceAspect(shutterResultRepository);
    }

    @Test
    @DisplayName("Should save ShutterResultEntity when ShutterResponseDTO is returned")
    void shouldSaveEntityWhenDtoReturned() {
        // Given
        ShutterResponseDTO responseDto = createShutterResponseDTO();

        // When
        persistenceAspect.saveResponse(responseDto);

        // Then
        ArgumentCaptor<ShutterResultEntity> entityCaptor = ArgumentCaptor.forClass(ShutterResultEntity.class);
        verify(shutterResultRepository).save(entityCaptor.capture());

        ShutterResultEntity savedEntity = entityCaptor.getValue();
        assertThat(savedEntity.getFileName()).isEqualTo("test-image.jpg");
        assertThat(savedEntity.getShutterCount()).isEqualTo(12345);
        assertThat(savedEntity.getCameraModel()).isEqualTo("Canon EOS R5");
        assertThat(savedEntity.getCreatedAt()).isNotNull();
        assertThat(savedEntity.getCreatedAt()).isBefore(LocalDateTime.now().plusSeconds(1));
    }

    @Test
    @DisplayName("Should handle null shutter count gracefully")
    void shouldHandleNullShutterCount() {
        // Given
        ShutterResponseDTO responseDto = ShutterResponseDTO.builder()
                .fileName("test-image.jpg")
                .shutterCount(null)
                .cameraModel("Canon EOS R5")
                .build();

        // When
        persistenceAspect.saveResponse(responseDto);

        // Then
        ArgumentCaptor<ShutterResultEntity> entityCaptor = ArgumentCaptor.forClass(ShutterResultEntity.class);
        verify(shutterResultRepository).save(entityCaptor.capture());

        ShutterResultEntity savedEntity = entityCaptor.getValue();
        assertThat(savedEntity.getShutterCount()).isNull();
    }

    @Test
    @DisplayName("Should handle null camera model gracefully")
    void shouldHandleNullCameraModel() {
        // Given
        ShutterResponseDTO responseDto = ShutterResponseDTO.builder()
                .fileName("test-image.jpg")
                .shutterCount(12345)
                .cameraModel(null)
                .build();

        // When
        persistenceAspect.saveResponse(responseDto);

        // Then
        ArgumentCaptor<ShutterResultEntity> entityCaptor = ArgumentCaptor.forClass(ShutterResultEntity.class);
        verify(shutterResultRepository).save(entityCaptor.capture());

        ShutterResultEntity savedEntity = entityCaptor.getValue();
        assertThat(savedEntity.getCameraModel()).isNull();
    }

    @Test
    @DisplayName("Should not fail when repository throws exception")
    void shouldNotFailWhenRepositoryThrowsException() {
        // Given
        ShutterResponseDTO responseDto = createShutterResponseDTO();
        doThrow(new RuntimeException("Database error")).when(shutterResultRepository).save(any());

        // When & Then - should not throw exception
        persistenceAspect.saveResponse(responseDto);

        verify(shutterResultRepository).save(any(ShutterResultEntity.class));
    }

    @Test
    @DisplayName("Should create entity with current timestamp")
    void shouldCreateEntityWithCurrentTimestamp() {
        // Given
        ShutterResponseDTO responseDto = createShutterResponseDTO();
        LocalDateTime beforeCall = LocalDateTime.now();

        // When
        persistenceAspect.saveResponse(responseDto);

        // Then
        LocalDateTime afterCall = LocalDateTime.now();
        ArgumentCaptor<ShutterResultEntity> entityCaptor = ArgumentCaptor.forClass(ShutterResultEntity.class);
        verify(shutterResultRepository).save(entityCaptor.capture());

        ShutterResultEntity savedEntity = entityCaptor.getValue();
        assertThat(savedEntity.getCreatedAt()).isAfter(beforeCall.minusSeconds(1));
        assertThat(savedEntity.getCreatedAt()).isBefore(afterCall.plusSeconds(1));
    }

    private ShutterResponseDTO createShutterResponseDTO() {
        return ShutterResponseDTO.builder()
                .fileName("test-image.jpg")
                .shutterCount(12345)
                .cameraModel("Canon EOS R5")
                .build();
    }
}