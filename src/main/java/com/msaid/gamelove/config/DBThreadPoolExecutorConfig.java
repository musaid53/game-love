package com.msaid.gamelove.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAsync
@RequiredArgsConstructor
public class DBThreadPoolExecutorConfig {
    private final GameLoveProperties gameLoveProperties;
    public static final String DEFAULT_DB_EXECUTOR_BEAN = "DefaultDbExecutor";


    @Bean(DEFAULT_DB_EXECUTOR_BEAN)
    public DBThreadPoolExecutor defaultDbExecutor(){
        GameLoveProperties.DBThreadPoolProperties dbThreadPoolProperties = gameLoveProperties.getDbProperties();
        return new DBThreadPoolExecutor(dbThreadPoolProperties.getCorePoolSize(),
                dbThreadPoolProperties.getMaxPoolSize(), dbThreadPoolProperties.getThreadNamePrefix(), dbThreadPoolProperties.getQueCapacity());
    }
}
