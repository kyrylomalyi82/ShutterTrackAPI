package com.kyrylomalyi.shuttertrackapi.web;


import com.kyrylomalyi.shuttertrackapi.dto.ShutterResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class ShutterController {

    @PostMapping("api/raw/upload")
    public ResponseEntity<ShutterResponseDTO> uploadImage(@RequestParam("file") MultipartFile file) {
        ShutterResponseDTO testDto = ShutterResponseDTO.builder()
                .fileName(file.getOriginalFilename())
                .cameraModel("Canon EOS 5D Mark IV")
                .shutterCount(12500)
                .build();

        return ResponseEntity.ok(testDto);
    }

}
