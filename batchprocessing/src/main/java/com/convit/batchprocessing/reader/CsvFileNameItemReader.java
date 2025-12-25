package com.convit.batchprocessing.reader;

import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;

@Component
public class CsvFileNameItemReader implements ItemReader<String> {

    private final Iterator<String> fileIterator;

    public CsvFileNameItemReader(@Value("${input.folder.vehicles}") Resource[] resources) {
        List<String> files = List.of(resources).stream()
                .map(resource -> {
                    try {
                        return resource.getFile().getAbsolutePath(); // full path
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .toList();

        this.fileIterator = files.iterator();
    }

    @Override
    public String read() {
        return fileIterator.hasNext() ? fileIterator.next() : null;
    }
}