package com.seong.shoutlink.global.config;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@EnableAsync
@Configuration
public class AsyncConfig {

    private static final int GENERATIVE_AI_CORE_POOL_SIZE = 3;
    private static final int GENERATIVE_AI_MAX_POOL_SIZE = 3;
    private static final String GENERATIVE_AI_THREAD_NAME_PREFIX = "GenerativeAITask-";
    private static final int DEFAULT_CORE_POOL_SIZE = 10;
    private static final int DEFAULT_MAX_POOL_SIZE = 10;
    private static final String DEFAULT_THREAD_NAME_PREFIX = "DefaultTask-";
    private static final int DEFAULT_QUEUE_CAPACITY = 50;
    private static final boolean WAIT_TASK_COMPLETION = true;

    @Bean(name = "generativeTaskExecutor")
    public Executor generativeTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(GENERATIVE_AI_CORE_POOL_SIZE);
        executor.setMaxPoolSize(GENERATIVE_AI_MAX_POOL_SIZE);
        executor.setQueueCapacity(DEFAULT_QUEUE_CAPACITY);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setWaitForTasksToCompleteOnShutdown(WAIT_TASK_COMPLETION);
        executor.setThreadNamePrefix(GENERATIVE_AI_THREAD_NAME_PREFIX);
        executor.initialize();
        return executor;
    }

    @Bean
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(DEFAULT_CORE_POOL_SIZE);
        executor.setMaxPoolSize(DEFAULT_MAX_POOL_SIZE);
        executor.setQueueCapacity(DEFAULT_QUEUE_CAPACITY);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setWaitForTasksToCompleteOnShutdown(WAIT_TASK_COMPLETION);
        executor.setThreadNamePrefix(DEFAULT_THREAD_NAME_PREFIX);
        executor.initialize();
        return executor;
    }
}
