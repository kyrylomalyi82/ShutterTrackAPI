package com.kyrylomalyi.shuttertrackapi.repository;

import com.kyrylomalyi.shuttertrackapi.entity.ShutterResultEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ShutterResultRepository extends JpaRepository<ShutterResultEntity, Long> {
    @Query("SELECT AVG(s.shutterCount) FROM ShutterResultEntity s")
    Long findAverageShutterCount();

    @Query("SELECT s.cameraModel FROM ShutterResultEntity s GROUP BY s.cameraModel ORDER BY COUNT(s) DESC LIMIT 1")
    String findMostFrequentCamera();
}
