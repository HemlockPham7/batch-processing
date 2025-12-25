package com.convit.batchprocessing.processor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FileNameProcessor implements ItemProcessor<String, String> {

    @Override
    public String process(String filePath) {
        log.info("Processing file: {}", filePath);
        return filePath;
    }
}