package com.ktc.matgpt.chatgpt.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {

    // core 사이즈만큼의 스레드에서 작업을 처리할 수 없다면, Queue에서 대기.
    // Queue가 full이라면, max size만큼 스레드 생성하여 처리
    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(4); // 기본 스레드 수
        executor.setMaxPoolSize(30); // 최대 스레드 수
        executor.setQueueCapacity(100); // Queue 사이즈
        executor.setThreadNamePrefix("Async-");
        executor.initialize();
        return executor;
    }
}
