package com.kyrylomalyi.shuttertrackapi.repository;

import com.kyrylomalyi.shuttertrackapi.entity.ShutterResultEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShutterResultRepository extends JpaRepository<ShutterResultEntity, Long> {

}
