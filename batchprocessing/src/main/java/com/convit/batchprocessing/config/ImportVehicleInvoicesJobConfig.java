package com.convit.batchprocessing.config;

import com.convit.batchprocessing.dto.VehicleDTO;
import com.convit.batchprocessing.listener.CustomJobExecutionListener;
import com.convit.batchprocessing.reader.MultiResourceReaderThreadSafe;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.ResourceAwareItemReaderItemStream;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.MultiResourceItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.task.VirtualThreadTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class ImportVehicleInvoicesJobConfig {

    @Value("${input.folder.vehicles}")
    private Resource[] resources;

    private final CustomJobExecutionListener customJobExecutionListener;

    @Bean
    public Job importVehicleJob(JobRepository repository, Step importVehicleStep) {
        return new JobBuilder("importVehicleJob", repository)
                .incrementer(new RunIdIncrementer())
                .start(importVehicleStep)
                .listener(customJobExecutionListener)
                .build();
    }

    @Bean
    public Step importVehicleStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("importVehicleStep", jobRepository)
                .<VehicleDTO, VehicleDTO>chunk(100, transactionManager)
                .reader(multiResourceReaderThreadSafe())
                .processor(ImportVehicleInvoicesJobConfig::vehicleProcessor)
                .writer(items -> log.info("writing item: {}", items))
                .taskExecutor(taskExecutor())
                .build();
    }

    private static VehicleDTO vehicleProcessor(VehicleDTO item) {
        log.info("Processing the item: {}", item);
        return item;
    }

    public MultiResourceReaderThreadSafe<VehicleDTO> multiResourceReaderThreadSafe() {
        var multiResourceReader = new MultiResourceReaderThreadSafe<>(multiResourceItemReader());
        multiResourceReader.setResources(resources);
        return multiResourceReader;
    }

    @Bean
    public MultiResourceItemReader<VehicleDTO> multiResourceItemReader() {
        return new MultiResourceItemReaderBuilder<VehicleDTO>()
                .name("vehicle resources reader")
                .resources(resources)
                .delegate(vehicleFlatFileItemReader())
                .build();

    }

    @Bean
    public ResourceAwareItemReaderItemStream<VehicleDTO> vehicleFlatFileItemReader() {
        return new FlatFileItemReaderBuilder<VehicleDTO>()
                .name("vehicle item reader")
                .saveState(Boolean.FALSE)
                .linesToSkip(1)
                .delimited()
                .delimiter(";")
                .names("referenceNumber", "model", "type", "customerFullName")
                .comments("#")
                .targetType(VehicleDTO.class)
                .build();
    }

    public VirtualThreadTaskExecutor taskExecutor() {
        return new VirtualThreadTaskExecutor("Custom-Thread-");
    }
}
