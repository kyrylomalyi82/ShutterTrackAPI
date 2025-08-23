package com.kyrylomalyi.shuttertrackapi.service;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifDirectoryBase;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.exif.makernotes.*;
import com.kyrylomalyi.shuttertrackapi.dto.ShutterResponseDTO;
import com.kyrylomalyi.shuttertrackapi.exceptions.MetadataExtractionException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

import java.util.Optional;

@Service
public class ShutterServiceImpl implements ShutterService {

    @Override
    public ShutterResponseDTO extractShutterCount(MultipartFile file) {
        try (InputStream inputStream = file.getInputStream()) {
            Metadata metadata = ImageMetadataReader.readMetadata(inputStream);
            ExifIFD0Directory directory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
            String manufacturer = directory.getString(ExifDirectoryBase.TAG_MAKE);


            return ShutterResponseDTO.builder()
                    .shutterCount(extractShutterCountByManufacturer(metadata, manufacturer))
                    .fileName(file.getOriginalFilename())
                    .cameraModel(directory.getString(ExifDirectoryBase.TAG_MODEL))
                    .build();

        } catch (IOException | MetadataExtractionException | ImageProcessingException e) {
            throw new MetadataExtractionException(e.getMessage());
        }
    }

    private Integer extractShutterCountByManufacturer(Metadata metadata, String manufacturer) {
        return switch (manufacturer.toUpperCase()) {
            case "NIKON CORPORATION" -> extractNikonShutterCount(metadata);
            default -> null;
        };
    }


    private Integer extractNikonShutterCount(Metadata metadata) {
        NikonType2MakernoteDirectory nikonDir = metadata.getFirstDirectoryOfType(NikonType2MakernoteDirectory.class);
        if (nikonDir == null) return null;

        return getIntegerFromDirectory(nikonDir, 0x00A7)
                .or(() -> getIntegerFromDirectory(nikonDir, 0x00A0))
                .orElse(null);
    }

    private Optional<Integer> getIntegerFromDirectory(Directory directory, int tag) {
        try {
            if (directory.containsTag(tag)) {
                return Optional.ofNullable(directory.getInteger(tag));
            }
        } catch (Exception e) {
            throw new MetadataExtractionException(e.getMessage());
        }
        return Optional.empty();
    }


    }
