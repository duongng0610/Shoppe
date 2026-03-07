package com.e_cormerce.shoppe.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class ThreadConfig {
    @Bean("uploadExecutor")
    public Executor asyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10); //số thread chạy song song mặc định
        executor.setMaxPoolSize(20);//số thread tối đa
        executor.setQueueCapacity(50);//số task chờ
        executor.setThreadNamePrefix("uploadExecutor-");
        executor.initialize();
    return executor;

    }
}
