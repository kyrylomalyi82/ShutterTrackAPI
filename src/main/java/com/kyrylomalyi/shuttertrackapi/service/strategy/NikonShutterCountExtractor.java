package com.kyrylomalyi.shuttertrackapi.service.strategy;

import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.makernotes.NikonType2MakernoteDirectory;
import com.kyrylomalyi.shuttertrackapi.exceptions.MetadataExtractionException;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

/**
 * Strategy implementation for extracting shutter count from Nikon NEF image files.
 *
 * <p>Nikon stores shutter count information directly in makernote EXIF tags, making
 * extraction straightforward compared to other manufacturers. This extractor supports
 * modern Nikon DSLR and mirrorless cameras using the Type2 makernote format.</p>
 *
 * <p>Supported formats: Nikon NEF (Nikon Electronic Format)</p>
 */
@Component
public class NikonShutterCountExtractor implements ShutterCountExtractorStrategy {

    private static final String NIKON_MANUFACTURER = "NIKON CORPORATION";

    // Primary and fallback EXIF tags for shutter count
    private static final int PRIMARY_SHUTTER_COUNT_TAG = 0x00A7;
    private static final int FALLBACK_SHUTTER_COUNT_TAG = 0x00A0;

    /**
     * Determines if this extractor supports the given camera manufacturer.
     *
     * @param manufacturer the camera manufacturer name from EXIF data
     * @return true if the manufacturer is Nikon Corporation (case-insensitive)
     */
    @Override
    public boolean supports(String manufacturer) {
        return NIKON_MANUFACTURER.equalsIgnoreCase(manufacturer);
    }

    /**
     * Extracts the shutter count from Nikon NEF file metadata.
     *
     * <p>Attempts extraction from two known EXIF tags in order of preference:
     * <ol>
     *   <li>Tag 0x00A7 - Primary shutter count tag used by most modern Nikon cameras</li>
     *   <li>Tag 0x00A0 - Fallback tag used by some camera models or firmware versions</li>
     * </ol></p>
     *
     * @param metadata the complete metadata object extracted from the NEF file
     * @return Optional containing the shutter count if found, empty otherwise
     * @throws MetadataExtractionException if an error occurs during metadata processing
     */
    @Override
    public Optional<Integer> extract(Metadata metadata) {
        NikonType2MakernoteDirectory nikonDir = metadata.getFirstDirectoryOfType(NikonType2MakernoteDirectory.class);
        if (nikonDir == null) return Optional.empty();

        return Objects.requireNonNull(getIntegerFromDirectory(nikonDir, PRIMARY_SHUTTER_COUNT_TAG)
                .or(() -> getIntegerFromDirectory(nikonDir, FALLBACK_SHUTTER_COUNT_TAG))
                .orElse(null)).describeConstable();
    }

    /**
     * Safely extracts an integer value from a specific EXIF tag.
     *
     * @param directory the makernote directory to read from
     * @param tag the EXIF tag identifier
     * @return Optional containing the integer value if present, empty otherwise
     * @throws MetadataExtractionException if tag reading fails
     */
    private Optional<Integer> getIntegerFromDirectory(Directory directory, int tag) {
        try {
            if (directory.containsTag(tag)) {
                return Optional.ofNullable(directory.getInteger(tag));
            }
        } catch (Exception e) {
            throw new MetadataExtractionException("Failed to extract integer from tag 0x" +
                    Integer.toHexString(tag).toUpperCase() + ": " + e.getMessage());
        }
        return Optional.empty();
    }
}