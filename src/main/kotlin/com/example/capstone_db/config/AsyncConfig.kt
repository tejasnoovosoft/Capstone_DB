package com.example.capstone_db.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor

@Configuration
@EnableAsync
class AsyncConfig {
    @Bean(name = ["threadPoolTaskExecutor"])
    fun getThreadPoolExecutor(): ThreadPoolTaskExecutor {
        val executor = ThreadPoolTaskExecutor()
        executor.corePoolSize = 20
        executor.maxPoolSize = 20
        executor.queueCapacity = 200
        executor.setThreadNamePrefix("MyThread-")
        executor.initialize()
        return executor
    }
}