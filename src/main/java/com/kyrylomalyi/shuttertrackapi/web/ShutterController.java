package com.kyrylomalyi.shuttertrackapi.web;

import com.kyrylomalyi.shuttertrackapi.dto.ShutterResponseDTO;
import com.kyrylomalyi.shuttertrackapi.service.ShutterService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * REST controller for handling RAW camera file uploads and retrieving
 * shutter count and camera model information.
 *
 * <p>This controller accepts a multipart file upload, delegates processing
 * to {@link ShutterService} to extract metadata, and returns a
 * {@link ShutterResponseDTO} with the results.</p>
 */
@RestController
public class ShutterController {

    private final ShutterService shutterService;

    /**
     * Constructs a new ShutterController with the provided service.
     *
     * @param shutterService the service responsible for extracting metadata from RAW files
     */
    public ShutterController(ShutterService shutterService) {
        this.shutterService = shutterService;
    }

    /**
     * Handles POST requests for uploading a RAW file.
     *
     * <p>Endpoint: <code>/api/raw/upload</code></p>
     *
     * @param file the RAW file uploaded by the user
     * @return a {@link ResponseEntity} containing a {@link ShutterResponseDTO} with:
     *         the file name, camera model, and shutter count
     */
    @PostMapping("api/raw/upload")
    public ResponseEntity<ShutterResponseDTO> uploadImage(@RequestParam("file") MultipartFile file) {
        ShutterResponseDTO response = shutterService.extractShutterCount(file);
        return ResponseEntity.ok(response);
    }
}