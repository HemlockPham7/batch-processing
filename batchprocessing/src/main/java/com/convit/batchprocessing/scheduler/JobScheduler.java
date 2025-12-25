package com.convit.batchprocessing.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class JobScheduler {
    private final Job uploadCsvToS3Job;
    private final JobLauncher jobLauncher;

    @Scheduled(cron = "0/30 * * * * ?")
    @SneakyThrows
    public void runJob() {
        var params = new JobParametersBuilder()
                .addDate("runDate", new Date())
                .toJobParameters();

        jobLauncher.run(uploadCsvToS3Job, params);
    }
}