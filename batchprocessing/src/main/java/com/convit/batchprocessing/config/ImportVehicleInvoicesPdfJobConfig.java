//package com.convit.batchprocessing.config;
//
//import com.convit.batchprocessing.dto.VehiclePdfDTO;
//import com.convit.batchprocessing.listener.CustomJobExecutionListener;
//import com.convit.batchprocessing.processor.InvoicePdfItemProcessor;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.batch.core.Job;
//import org.springframework.batch.core.Step;
//import org.springframework.batch.core.job.builder.JobBuilder;
//import org.springframework.batch.core.launch.support.RunIdIncrementer;
//import org.springframework.batch.core.repository.JobRepository;
//import org.springframework.batch.core.step.builder.StepBuilder;
//import org.springframework.batch.item.file.ResourcesItemReader;
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
//public class ImportVehicleInvoicesPdfJobConfig {
//
//    @Value("${input.folder.vehicles.pdf}")
//    private Resource[] resources;
//
//    private final CustomJobExecutionListener customJobExecutionListener;
//    private final InvoicePdfItemProcessor invoicePdfItemProcessor;
//
//    @Bean
//    public Job importVehiclePdfJob(JobRepository repository, Step importVehiclePdfStep) {
//        return new JobBuilder("importVehiclePdfJob", repository)
//                .incrementer(new RunIdIncrementer())
//                .start(importVehiclePdfStep)
//                .listener(customJobExecutionListener)
//                .build();
//    }
//
//    @Bean
//    public Step importVehiclePdfStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
//        return new StepBuilder("importVehiclePdfStep", jobRepository)
//                .<Resource, VehiclePdfDTO>chunk(2, transactionManager)
//                .reader(resourcesItemReader())
//                .processor(invoicePdfItemProcessor)
//                .writer(items -> log.info("writing item: {}", items))
//                .taskExecutor(taskExecutor())
//                .build();
//    }
//
//    public ResourcesItemReader resourcesItemReader() {
//        var resourcesItemReader = new ResourcesItemReader();
//         resourcesItemReader.setResources(resources);
//         return resourcesItemReader;
//    }
//
//    public VirtualThreadTaskExecutor taskExecutor() {
//        return new VirtualThreadTaskExecutor("Json-Thread-");
//    }
//}
