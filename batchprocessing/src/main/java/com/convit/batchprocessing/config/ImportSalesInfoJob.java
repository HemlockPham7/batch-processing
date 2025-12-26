package com.convit.batchprocessing.config;

import com.convit.batchprocessing.domain.SalesInfo;
import com.convit.batchprocessing.dto.SalesDTO;
import com.convit.batchprocessing.task.DownloadFileTask;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.builder.TaskletStepBuilder;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class ImportSalesInfoJob {

    private final EntityManagerFactory entityManagerFactory;
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final DownloadFileTask downloadFileTask;

    @Bean
    public Job syncSalesJob(Step downloadFileStep, Step fromFileDownloadedToDb) {
        return new JobBuilder("sync-sales-job", jobRepository)
                .incrementer(new RunIdIncrementer())
                .flow(downloadFileStep)
                .next(fromFileDownloadedToDb)
                .end()
                .build();

    }

    @Bean
    public Step downloadFileStep() {
        return new TaskletStepBuilder(new StepBuilder("downloadFileStep", jobRepository))
                .tasklet(downloadFileTask, transactionManager)
                .build();

    }

    @Bean
    public Step fromFileDownloadedToDb(FlatFileItemReader<SalesDTO> flatFileItemReader) {
        return new StepBuilder("fromFileDownloadedToDb", jobRepository)
                .<SalesDTO, SalesInfo>chunk(2000, transactionManager)
                .reader(flatFileItemReader)
                .processor(dto -> {
                    SalesInfo entity = new SalesInfo();
                    entity.setSaleId(dto.saleId());
                    entity.setProductId(dto.productId());
                    entity.setCustomerId(dto.customerId());
                    entity.setSaleDate(dto.saleDate());
                    entity.setSaleAmount(dto.saleAmount());
                    entity.setLocation(dto.location());
                    entity.setCountry(dto.country());
                    entity.setProcessed(dto.processed());
                    return entity;
                })
                .writer(salesJpaWriter())
                .build();
    }

    @Bean
    @JobScope
    public FlatFileItemReader<SalesDTO> flatFileItemReader(@Value("#{jobExecutionContext['input.file.path']}") String resource) {
        return new FlatFileItemReaderBuilder<SalesDTO>()
                .name("fileReader")
                .resource(new FileSystemResource(resource))
                .delimited()
                .delimiter(",")
                .names("saleId", "productId", "customerId", "saleDate", "saleAmount", "location", "country", "processed")
                .targetType(SalesDTO.class)
                .linesToSkip(1)
                .saveState(Boolean.TRUE)
                .build();
    }

    @Bean
    public JpaItemWriter<SalesInfo> salesJpaWriter() {
        return new JpaItemWriterBuilder<SalesInfo>()
                .entityManagerFactory(entityManagerFactory)
                .build();
    }
}