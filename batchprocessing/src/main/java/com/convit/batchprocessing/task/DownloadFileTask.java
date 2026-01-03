package com.convit.batchprocessing.task;

import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Timer;
import com.convit.batchprocessing.service.CustomS3Client;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class DownloadFileTask implements Tasklet {

    private final CustomS3Client customS3Client;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucketName;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

        Timer.Sample sample = Timer.start(Metrics.globalRegistry);

        Thread.sleep(5000L);

        Optional<Path> optionalDownloadedFilePath = customS3Client.download(bucketName);

        optionalDownloadedFilePath.ifPresent(filePath ->
                chunkContext.getStepContext()
                        .getStepExecution()
                        .getJobExecution()
                        .getExecutionContext()
                        .put("input.file.path", filePath.toAbsolutePath().toString()));

        sample.stop(Timer.builder("spring_batch_step_download_file_custom_metrics")
                .description("Custom Metrics for Download File")
                .tag("bucket", bucketName)
                .tag("status",contribution.getExitStatus().getExitCode())
                .register(Metrics.globalRegistry));

        return RepeatStatus.FINISHED;
    }
}