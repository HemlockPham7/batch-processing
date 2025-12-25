//package com.convit.batchprocessing.config;
//
//import com.convit.batchprocessing.dto.VehicleForJsonDTO;
//import com.convit.batchprocessing.listener.CustomJobExecutionListener;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.batch.core.Job;
//import org.springframework.batch.core.Step;
//import org.springframework.batch.core.job.builder.JobBuilder;
//import org.springframework.batch.core.launch.support.RunIdIncrementer;
//import org.springframework.batch.core.repository.JobRepository;
//import org.springframework.batch.core.step.builder.StepBuilder;
//import org.springframework.batch.item.file.MultiResourceItemReader;
//import org.springframework.batch.item.file.ResourceAwareItemReaderItemStream;
//import org.springframework.batch.item.file.builder.MultiResourceItemReaderBuilder;
//import org.springframework.batch.item.json.JacksonJsonObjectReader;
//import org.springframework.batch.item.json.builder.JsonItemReaderBuilder;
//import org.springframework.batch.item.support.SynchronizedItemStreamReader;
//import org.springframework.batch.item.support.builder.SynchronizedItemStreamReaderBuilder;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.io.Resource;
//import org.springframework.core.task.VirtualThreadTaskExecutor;
//import org.springframework.transaction.PlatformTransactionManager;
//
//@Slf4j
//@Configuration
//@RequiredArgsConstructor
//public class ImportVehicleInvoicesJsonJobConfig {
//
//    @Value("${input.folder.vehicles.json}")
//    private Resource[] resources;
//
//    private final CustomJobExecutionListener customJobExecutionListener;
//
//    @Bean
//    public Job importVehicleJsonJob(JobRepository repository, Step importVehicleJsonStep) {
//        return new JobBuilder("importVehicleJsonJob", repository)
//                .incrementer(new RunIdIncrementer())
//                .start(importVehicleJsonStep)
//                .listener(customJobExecutionListener)
//                .build();
//    }
//
//    @Bean
//    public Step importVehicleJsonStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
//        return new StepBuilder("importVehicleJsonStep", jobRepository)
//                .<VehicleForJsonDTO, VehicleForJsonDTO>chunk(100, transactionManager)
//                .reader(synchronizedItemStreamReader())
//                .processor(ImportVehicleInvoicesJsonJobConfig::vehicleProcessor)
//                .writer(items -> log.info("writing item: {}", items))
//                .taskExecutor(taskExecutor())
//                .build();
//    }
//
//    private static VehicleForJsonDTO vehicleProcessor(VehicleForJsonDTO item) {
//        log.info("Processing the item: {}", item);
//        return item;
//    }
//
//    @Bean
//    public SynchronizedItemStreamReader<VehicleForJsonDTO> synchronizedItemStreamReader() {
//        return new SynchronizedItemStreamReaderBuilder<VehicleForJsonDTO>()
//                .delegate(multiResourceJsonItemReader())
//                .build();
//    }
//
//    @Bean
//    public MultiResourceItemReader<VehicleForJsonDTO> multiResourceJsonItemReader() {
//        return new MultiResourceItemReaderBuilder<VehicleForJsonDTO>()
//                .name("vehicle resources reader")
//                .resources(resources)
//                .delegate(jsonItemReader())
//                .build();
//
//    }
//
//    @Bean
//    public ResourceAwareItemReaderItemStream<VehicleForJsonDTO> jsonItemReader() {
//        return new JsonItemReaderBuilder<VehicleForJsonDTO>()
//                .name("vehicle json reader")
//                .jsonObjectReader(new JacksonJsonObjectReader<>(VehicleForJsonDTO.class))
//                .strict(false)
//                .build();
//    }
//
//    public VirtualThreadTaskExecutor taskExecutor() {
//        return new VirtualThreadTaskExecutor("Json-Thread-");
//    }
//}
