# batch-processing

## Java Send Mail

```
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-mail</artifactId>
    </dependency>
```

## Docker set up for mail hog

```
services:
  mailhog:
    image: mailhog/mailhog
    container_name: mailhog-container
    restart: unless-stopped
    ports:
      - "1025:1025" # SMTP server port
      - "8025:8025" # Web interface port

```

## Communication betwen step in batch job

**Step 1**: write data into StepExecutionContext

```java
stepExecution.getExecutionContext().put("totalCount", 100);
```

**Promotion Listener**

```java
@Bean
public ExecutionContextPromotionListener promotionListener() {
    ExecutionContextPromotionListener listener =
            new ExecutionContextPromotionListener();
    listener.setKeys(new String[]{"totalCount"});
    return listener;
}

```

**Attach listener into Step 1**

```java
@Bean
public Step step1() {
    return stepBuilderFactory.get("step1")
            .tasklet(tasklet1())
            .listener(promotionListener())
            .build();
}

```


➡️ After step 1 is done, key totalCount will be promoted in JobExecutionContext

**Step 2**: Read data

```java
@Value("#{jobExecutionContext['totalCount']}")
private Integer totalCount;

```

Or:

```java
stepExecution.getJobExecution()
    .getExecutionContext()
    .get("totalCount");

```