package com.convit.batchprocessing.config;

import com.convit.batchprocessing.processor.FileNameProcessor;
import com.convit.batchprocessing.reader.CsvFileNameItemReader;
import com.convit.batchprocessing.writer.CsvFileUploadItemWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@EnableBatchProcessing
@Configuration
@RequiredArgsConstructor
public class UploadCsvToS3JobConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final CsvFileNameItemReader reader;
    private final FileNameProcessor processor;
    private final CsvFileUploadItemWriter writer;

    @Bean
    public Job uploadCsvToS3Job() {
        return new JobBuilder("uploadCsvToS3Job", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(uploadCsvStep())
                .build();
    }

    @Bean
    public Step uploadCsvStep() {
        return new StepBuilder("uploadCsvStep", jobRepository)
                .<String, String>chunk(4, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }
}
