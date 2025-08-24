package com.kyrylomalyi.shuttertrackapi.service.strategy;

import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.makernotes.NikonType2MakernoteDirectory;
import com.kyrylomalyi.shuttertrackapi.exceptions.MetadataExtractionException;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Component
public class NikonShutterCountExtractor implements  ShutterCountExtractorStrategy{

    private static final String NIKON_MANUFACTURER = "NIKON CORPORATION";

    @Override
    public boolean supports(String manufacturer) {
        return NIKON_MANUFACTURER.equalsIgnoreCase(manufacturer);
    }
    
    @Override
    public Optional<Integer> extract(Metadata metadata) {
        NikonType2MakernoteDirectory nikonDir = metadata.getFirstDirectoryOfType(NikonType2MakernoteDirectory.class);
        if (nikonDir == null) return Optional.empty();

        return Objects.requireNonNull(getIntegerFromDirectory(nikonDir, 0x00A7)
                .or(() -> getIntegerFromDirectory(nikonDir, 0x00A0))
                .orElse(null)).describeConstable();
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
