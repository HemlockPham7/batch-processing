package com.convit.batchprocessing.scheduler;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class JobTrigger {

    private final JobLauncher jobLauncher;
    private final Job job;

    public JobTrigger(
            JobLauncher jobLauncher,
            @Qualifier("importVehiclePdfJob") Job job
    ) {
        this.jobLauncher = jobLauncher;
        this.job = job;
    }

    @Scheduled(cron = "0/30 * * ? * *")
    @SneakyThrows
    void launchJobPeriodically() {
        log.info("===============> launching the job");

        var jobParameters = new JobParametersBuilder();
        jobParameters.addDate("uniqueness", new Date());
        JobExecution jobExecution = this.jobLauncher.run(job, jobParameters.toJobParameters());

        log.info("===============> job finished with the status: {}", jobExecution.getExitStatus());
    }

}
