package com.kyrylomalyi.shuttertrackapi.service.strategy;

import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.makernotes.CanonMakernoteDirectory;
import com.kyrylomalyi.shuttertrackapi.exceptions.MetadataExtractionException;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Strategy implementation for extracting shutter count from Canon CR2 image files.
 *
 * <p>Canon stores shutter count information in the Camera Info Array within the makernote metadata.
 * This extractor attempts to retrieve the count from various known locations within this array,
 * as different Canon camera models may store this data at different offsets.</p>
 *
 * <p>Supported formats: Canon CR2 (Canon Raw version 2)</p>
 */
@Component
public class CanonShutterCountExtractor implements ShutterCountExtractorStrategy {

    private static final String CANON_MANUFACTURER = "Canon";

    // Camera Info Array indices for shutter count extraction
    private static final int PRIMARY_SHUTTER_COUNT_START_INDEX = 149;
    private static final int PRIMARY_SHUTTER_COUNT_END_INDEX = 152;
    private static final int ALTERNATIVE_SHUTTER_COUNT_INDEX = 23;

    // Validation boundaries for shutter count values
    private static final int MIN_VALID_SHUTTER_COUNT = 1;
    private static final int MAX_VALID_SHUTTER_COUNT = 10_000_000;

    /**
     * Determines if this extractor supports the given camera manufacturer.
     *
     * @param manufacturer the camera manufacturer name from EXIF data
     * @return true if the manufacturer is Canon (case-insensitive), false otherwise
     */
    @Override
    public boolean supports(String manufacturer) {
        return CANON_MANUFACTURER.equalsIgnoreCase(manufacturer);
    }

    /**
     * Extracts the shutter count from Canon CR2 file metadata.
     *
     * <p>Attempts multiple extraction strategies in order of preference:
     * <ol>
     *   <li>Camera Info Array (primary method)</li>
     *   <li>Internal Serial Number tag (0x0093)</li>
     *   <li>Lens Serial Number area tag (0x0095)</li>
     * </ol></p>
     *
     * @param metadata the complete metadata object extracted from the image file
     * @return Optional containing the shutter count if successfully extracted, empty otherwise
     * @throws MetadataExtractionException if an error occurs during metadata processing
     */
    @Override
    public Optional<Integer> extract(Metadata metadata) {
        CanonMakernoteDirectory canonDir = metadata.getFirstDirectoryOfType(CanonMakernoteDirectory.class);
        if (canonDir == null) {
            return Optional.empty();
        }

        return getShutterCountFromCameraInfo(canonDir)
                .or(() -> getIntegerFromDirectory(canonDir, 0x0093)) // Internal Serial Number
                .or(() -> getIntegerFromDirectory(canonDir, 0x0095));
    }

    /**
     * Safely extracts an integer value from a metadata directory tag.
     *
     * @param directory the metadata directory to read from
     * @param tag the specific tag identifier to extract
     * @return Optional containing the integer value if present and valid, empty otherwise
     * @throws MetadataExtractionException if an error occurs during tag reading
     */
    private Optional<Integer> getIntegerFromDirectory(Directory directory, int tag) {
        try {
            if (directory.containsTag(tag)) {
                return Optional.ofNullable(directory.getInteger(tag));
            }
        } catch (Exception e) {
            throw new MetadataExtractionException(
                    String.format("Failed to extract integer from directory tag 0x%04X: %s", tag, e.getMessage()));
        }
        return Optional.empty();
    }

    /**
     * Extracts shutter count from Canon's proprietary Camera Info Array.
     *
     * <p>Canon cameras store detailed information in a Camera Info Array within the makernote.
     * The shutter count is typically stored as a 4-byte big-endian integer at specific array indices.
     * Different camera models may use different array positions, so multiple locations are checked.</p>
     *
     * <p>Primary extraction method (indices 149-152):
     * Used by most modern Canon DSLR models including EOS 5D series, 7D series, and 1D series.</p>
     *
     * <p>Alternative extraction method (index 23):
     * Used by some older or specific Canon models as a fallback option.</p>
     *
     * @param directory the Canon makernote directory containing the Camera Info Array
     * @return Optional containing the extracted shutter count if valid, empty otherwise
     * @throws MetadataExtractionException if an error occurs during array processing
     */
    private Optional<Integer> getShutterCountFromCameraInfo(CanonMakernoteDirectory directory) {
        try {
            if (!directory.containsTag(CanonMakernoteDirectory.TAG_CAMERA_INFO_ARRAY)) {
                return Optional.empty();
            }

            int[] cameraInfoArray = directory.getIntArray(CanonMakernoteDirectory.TAG_CAMERA_INFO_ARRAY);
            if (cameraInfoArray == null) {
                return Optional.empty();
            }

            // Primary extraction: 4-byte big-endian integer from indices 149-152
            if (cameraInfoArray.length > PRIMARY_SHUTTER_COUNT_END_INDEX) {
                int shutterCount = assembleBigEndianInteger(
                        cameraInfoArray[PRIMARY_SHUTTER_COUNT_START_INDEX],
                        cameraInfoArray[PRIMARY_SHUTTER_COUNT_START_INDEX + 1],
                        cameraInfoArray[PRIMARY_SHUTTER_COUNT_START_INDEX + 2],
                        cameraInfoArray[PRIMARY_SHUTTER_COUNT_END_INDEX]
                );

                if (isValidShutterCount(shutterCount)) {
                    return Optional.of(shutterCount);
                }
            }

            // Alternative extraction: single integer from index 23
            if (cameraInfoArray.length > ALTERNATIVE_SHUTTER_COUNT_INDEX) {
                int alternativeCount = cameraInfoArray[ALTERNATIVE_SHUTTER_COUNT_INDEX];
                if (isValidShutterCount(alternativeCount)) {
                    return Optional.of(alternativeCount);
                }
            }

        } catch (Exception e) {
            throw new MetadataExtractionException(
                    "Failed to extract shutter count from Camera Info Array: " + e.getMessage());
        }

        return Optional.empty();
    }

    /**
     * Assembles a 32-bit integer from four bytes in big-endian byte order.
     *
     * @param byte1 most significant byte (bits 31-24)
     * @param byte2 second byte (bits 23-16)
     * @param byte3 third byte (bits 15-8)
     * @param byte4 least significant byte (bits 7-0)
     * @return the assembled 32-bit integer value
     */
    private int assembleBigEndianInteger(int byte1, int byte2, int byte3, int byte4) {
        return (byte1 << 24) | (byte2 << 16) | (byte3 << 8) | byte4;
    }

    /**
     * Validates whether the extracted shutter count value is within reasonable bounds.
     *
     * <p>A valid shutter count should be:
     * <ul>
     *   <li>Greater than 0 (cameras start counting from 1)</li>
     *   <li>Less than 10,000,000 (reasonable upper bound for camera lifetime)</li>
     * </ul></p>
     *
     * @param count the shutter count value to validate
     * @return true if the count is within valid bounds, false otherwise
     */
    private boolean isValidShutterCount(int count) {
        return count >= MIN_VALID_SHUTTER_COUNT && count <= MAX_VALID_SHUTTER_COUNT;
    }
}