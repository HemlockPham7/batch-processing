# batch-multithread-upload

## Concurrency limits for virtual threads

```java
@Bean
public TaskExecutor batchTaskExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

    executor.setCorePoolSize(40);      // số chunk chạy song song
    executor.setMaxPoolSize(40);       // hard limit
    executor.setQueueCapacity(0);      // không queue, submit là chạy
    executor.setThreadNamePrefix("vt-batch-");

    // QUAN TRỌNG: bật virtual thread
    executor.setTaskDecorator(runnable ->
            Thread.ofVirtual().name("vt-batch-", 0).unstarted(runnable)
    );

    executor.initialize();
    return executor;
}

```

**Update UploadCsvToS3JobConfig**

```java
@Bean
public Step uploadCsvStep() {
    return new StepBuilder("uploadCsvStep", jobRepository)
            .<String, String>chunk(100, transactionManager)
            .reader(reader)          // thread-safe
            .processor(processor)    // stateless
            .writer(writer)          // thread-safe
            .taskExecutor(batchTaskExecutor())
            .throttleLimit(40)       // RẤT QUAN TRỌNG
            .build();
}
```


**Why throttleLimit is needed?**

- Spring Batch doesn't always trust the executor.
- throttleLimit = limit on the number of parallel chunks.
- Therefore, = maxPoolSize