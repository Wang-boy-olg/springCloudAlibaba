package org.example.utils;


import cn.hutool.core.thread.ThreadFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;

@Configuration
public class ThreadPoolUtils {


    public static int corePoolSize = 5;
    public static int maxPoolSize = 20;
    public static int keepAliveSeconds = 5;
    public static int queueCapacity = 40;

    @Bean(name = "executorService")
    public static ExecutorService getThreadPool() {
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNamePrefix("chc-thread-pool-%d").build();
        ThreadPoolExecutor pool = new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveSeconds,
                TimeUnit.SECONDS, new LinkedBlockingQueue<>(queueCapacity), namedThreadFactory,
                new ThreadPoolExecutor.CallerRunsPolicy());
        return pool;
    }
}