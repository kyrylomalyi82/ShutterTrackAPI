package com.kyrylomalyi.shuttertrackapi.service;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifDirectoryBase;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.exif.makernotes.*;
import com.kyrylomalyi.shuttertrackapi.dto.ShutterResponseDTO;
import com.kyrylomalyi.shuttertrackapi.exceptions.MetadataExtractionException;
import com.kyrylomalyi.shuttertrackapi.service.strategy.ShutterCountExtractorStrategy;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

import java.util.List;

@Service
public class ShutterServiceImpl implements ShutterService {

    public final List<ShutterCountExtractorStrategy> strategies;

    public ShutterServiceImpl(List<ShutterCountExtractorStrategy> strategies) {
        this.strategies = strategies;
        System.out.println(strategies);
    }

    @Override
    public ShutterResponseDTO extractShutterCount(MultipartFile file) {
        try (InputStream inputStream = file.getInputStream()) {
            Metadata metadata = ImageMetadataReader.readMetadata(inputStream);
            ExifIFD0Directory directory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
            String manufacturer = directory.getString(ExifDirectoryBase.TAG_MAKE);

            Integer shutterCount = findStrategy(manufacturer)
                    .extract(metadata)
                    .orElse(null); // Если Optional пустой, возвращаем null

            return ShutterResponseDTO.builder()
                    .shutterCount(shutterCount)
                    .fileName(file.getOriginalFilename())
                    .cameraModel(directory.getString(ExifDirectoryBase.TAG_MODEL))
                    .build();

        } catch (IOException | MetadataExtractionException | ImageProcessingException e) {
            throw new MetadataExtractionException(e.getMessage());
        }
    }

    private ShutterCountExtractorStrategy findStrategy(String manufacturer) {
        return strategies.stream()
                .filter(s -> s.supports(manufacturer))
                .findFirst()
                .orElseThrow(() -> new MetadataExtractionException("Unsupported file"));
    }
    }
