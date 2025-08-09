package com.tripcut.core.batch.quartz;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

@Configuration
public class QuartzConfig {

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean() {
        SchedulerFactoryBean schedulerFactory = new SchedulerFactoryBean();
        // 메모리 기반 스케줄러로 설정 (JDBC 제거)
        schedulerFactory.setAutoStartup(true);
        schedulerFactory.setOverwriteExistingJobs(true);
        schedulerFactory.setApplicationContextSchedulerContextKey("applicationContext");
        return schedulerFactory;
    }
} 