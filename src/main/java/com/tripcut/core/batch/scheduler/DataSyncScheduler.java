package com.tripcut.core.batch.scheduler;

import org.quartz.*;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class DataSyncScheduler {

    private final Scheduler scheduler;

    @PostConstruct
    public void init() {
        // 배치가 비활성화된 경우 스케줄러도 비활성화
        if (!isBatchEnabled()) {
            log.info("배치가 비활성화되어 있어 스케줄러를 등록하지 않습니다.");
            return;
        }
        
        try {
            JobDetail jobDetail = JobBuilder.newJob(DataSyncQuartzJob.class)
                    .withIdentity("dataSyncJob")
                    .build();

            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity("dataSyncTrigger")
                    .withSchedule(CronScheduleBuilder.cronSchedule("0 0 1 * * ?")) // 매일 새벽 1시에 실행
                    .build();

            scheduler.scheduleJob(jobDetail, trigger);
            log.info("데이터 동기화 스케줄러가 등록되었습니다.");
        } catch (SchedulerException e) {
            log.error("데이터 동기화 스케줄러 등록 실패", e);
        }
    }
    
    private boolean isBatchEnabled() {
        // Spring Boot의 batch.job.enabled 속성을 확인
        String batchEnabled = System.getProperty("spring.batch.job.enabled", "false");
        return Boolean.parseBoolean(batchEnabled);
    }
} 