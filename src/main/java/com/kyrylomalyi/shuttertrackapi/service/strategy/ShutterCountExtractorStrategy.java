package com.kyrylomalyi.shuttertrackapi.service.strategy;

import com.drew.metadata.Metadata;

import java.util.Optional;

public interface ShutterCountExtractorStrategy {

    Optional<Integer> extract(Metadata metadata);

    boolean supports(String manufacturer);

}
