package com.kyrylomalyi.shuttertrackapi.service;

import com.kyrylomalyi.shuttertrackapi.dto.ShutterResponseDTO;
import org.springframework.web.multipart.MultipartFile;

public interface ShutterService {
    ShutterResponseDTO extractShutterCount(MultipartFile file);
}
