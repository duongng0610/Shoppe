package com.e_cormerce.shoppe.configuration;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class ThreadConfig {
  @Bean("uploadExecutor")
  public Executor imageUploadExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

    executor.setCorePoolSize(20);
    executor.setMaxPoolSize(40);
    executor.setQueueCapacity(50);

    /**
     * Use hen number of thread from max pool size to core pool size, thread not used is destroyed
     * after...
     */
    executor.setKeepAliveSeconds(60);

    /**
     * Use when thread used full of slot
     *
     * <p>REJECTION POLICY = xử lý khi pool full + queue full. CallerRunsPolicy: task chạy trên
     * thread của caller (HTTP thread). AbortPolicy (default): throw RejectedExecutionException →
     * mất request
     */
    executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

    /**
     * Use when application is shutdown
     *
     * <p>Do the task in processing in 30s, not get new task
     */
    executor.setWaitForTasksToCompleteOnShutdown(true);
    executor.setAwaitTerminationSeconds(30);

    executor.setThreadNamePrefix("uploadExecutor-");
    executor.initialize();

    return executor;
  }

  @Bean("queryDBExecutor")
  public Executor queryDBExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

    executor.setCorePoolSize(20);
    executor.setMaxPoolSize(40);
    executor.setQueueCapacity(50);

    executor.setKeepAliveSeconds(60);
    executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
    executor.setWaitForTasksToCompleteOnShutdown(true);
    executor.setAwaitTerminationSeconds(30);

    executor.setThreadNamePrefix("queryDBExecutor-");
    executor.initialize();

    return executor;
  }
}
