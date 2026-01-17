package com.convit.batchprocessing.writer;

import com.convit.batchprocessing.service.StoreFileToObjectStorageUseCase;
import com.convit.batchprocessing.service.dto.ObjectDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
@Component
@RequiredArgsConstructor
public class CsvFileUploadItemWriter implements ItemWriter<String> {

    private final StoreFileToObjectStorageUseCase storageUseCase;

    @Override
    public void write(Chunk<? extends String> chunk) throws Exception {

        for (String filePathStr : chunk.getItems()) {
            Path path = Path.of(filePathStr);

            ObjectDTO objectDTO = new ObjectDTO(
                    path.getFileName().toString(),
                    Files.probeContentType(path),
                    Files.size(path),
                    path
            );

            log.info("Writing item (uploaded): {}", path.getFileName());
            storageUseCase.store(objectDTO);
        }
    }
}