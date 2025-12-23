package com.convit.batchprocessing.component.item;

import org.springframework.batch.item.ItemProcessor;

public class TestProcessor implements ItemProcessor<String, String> {
    @Override
    public String process(String item) {
        return item.toUpperCase();
    }
}
