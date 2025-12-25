package com.convit.batchprocessing.service.dto;

public record ObjectDTO(String name, String contentType, Long size, byte [] data) {
}