package com.kyrylomalyi.shuttertrackapi.util;

import com.kyrylomalyi.shuttertrackapi.exceptions.FileValidationException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.List;

@Component
public class ValidationUtil {

    @Value("${file.validation.max-size}")
    private long maxFileSize;

//    @Value("{$file.validation.allowed-types}") does not work :(
    @Value("#{'${file.validation.allowed-types}'.split(',')}")
    private List<String> allowedFileTypesList;

    private final HashSet<String> allowedFileTypes = new HashSet<>();

    @PostConstruct
    public void init() {
        allowedFileTypesList.forEach(type -> allowedFileTypes.add(type.toUpperCase()));
    }

    public void validateFile(MultipartFile file) {
        String fileExtension = StringUtils.getFilenameExtension(file.getOriginalFilename());

        if (!isSupportedFile(fileExtension)) {
            throw new FileValidationException("The file is not supported");
        }

        if (isFileSizeNotHigherLimit(file)) {
            throw new FileValidationException("The file is too large");
        }


    }

    private boolean isFileSizeNotHigherLimit (MultipartFile file) {
        return file.getSize() > maxFileSize;
    }

    private boolean isSupportedFile (String fileExtension) {
        return fileExtension != null && allowedFileTypes.contains(fileExtension.toUpperCase());
    }

}
