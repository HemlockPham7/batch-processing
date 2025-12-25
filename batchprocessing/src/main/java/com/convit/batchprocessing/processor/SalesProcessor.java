package com.convit.batchprocessing.processor;

import com.convit.batchprocessing.dto.SalesDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SalesProcessor implements ItemProcessor<SalesDTO, SalesDTO> {
    @Override
    public SalesDTO process(SalesDTO item) throws Exception {
        log.info("processing the item: {}", item);
        if ("United States".equalsIgnoreCase(item.country())) {
            return null;
        }
        return item;
    }
}