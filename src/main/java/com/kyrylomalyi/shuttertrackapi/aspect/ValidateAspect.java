package com.kyrylomalyi.shuttertrackapi.aspect;

import com.kyrylomalyi.shuttertrackapi.util.ValidationUtil;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@Aspect
public class ValidateAspect {

    private final ValidationUtil validationUtil;

    public ValidateAspect(ValidationUtil validationUtil) {
        this.validationUtil = validationUtil;
    }

    @Before("execution(* com.kyrylomalyi.shuttertrackapi.web.*.*(..)) && args(file)")
    public void validateFileUpload(MultipartFile file) {
        validationUtil.validateFile(file);
    }
}
