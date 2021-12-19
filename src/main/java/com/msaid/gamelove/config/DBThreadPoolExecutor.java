package com.msaid.gamelove.config;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

public class DBThreadPoolExecutor extends ThreadPoolTaskExecutor {
    public DBThreadPoolExecutor(int corePoolSize, int maxPoolSize, String threadNamePrefix, int queCapacity){
        super();
        setCorePoolSize(corePoolSize);
        setMaxPoolSize(maxPoolSize);
        setThreadNamePrefix(threadNamePrefix);
        setQueueCapacity(queCapacity);
        initialize();
    }

}
