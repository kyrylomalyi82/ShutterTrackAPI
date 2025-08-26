package com.kyrylomalyi.shuttertrackapi.repository;

import com.kyrylomalyi.shuttertrackapi.entity.ShutterResultEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ShutterResultRepositoryTest {

    @Autowired
    private ShutterResultRepository shutterResultRepository;

    @Test
    void saveAndFindById() {
        ShutterResultEntity entity = ShutterResultEntity.builder()
                .fileName("test.cr2")
                .shutterCount(123)
                .createdAt(LocalDateTime.now())
                .build();

        ShutterResultEntity saved = shutterResultRepository.save(entity);

        assertNotNull(saved);

        ShutterResultEntity found = shutterResultRepository.findById(saved.getId()).orElseThrow();
        assertEquals(saved.getId(), found.getId() );
        assertEquals(saved.getShutterCount(), found.getShutterCount() );

    }

}