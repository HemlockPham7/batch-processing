package com.convit.batchprocessing.service.dto;

import java.nio.file.Path;

public record ObjectDTO(
        String name,
        String contentType,
        Long size,
        Path path
) {}